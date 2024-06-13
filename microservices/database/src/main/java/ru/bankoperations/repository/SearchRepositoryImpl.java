package ru.bankoperations.repository;

import jooq.db.Tables;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.bankoperations.dto.*;
import ru.bankoperations.repository.mapper.PersonDtoPersonRecordMapping;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

@Repository
@AllArgsConstructor
@Slf4j
public class SearchRepositoryImpl implements SearchRepository {
    private final DSLContext dsl;

    @Override
    public List<Long> searchPerson(
            String birthDate,
            String phoneNumber,
            String firstName,
            String lastName,
            String surName,
            String emailAddress,
            String login) {

        Condition searchCondition = getConditions(
                birthDate,
                phoneNumber,
                firstName,
                lastName,
                surName,
                emailAddress);

        return dsl
                .select(Tables.PERSON.ID)
                .from(Tables.PERSON)
                .join(Tables.PHONE)
                .on(Tables.PHONE.PERSON_ID.eq(Tables.PERSON.ID))
                .join(Tables.EMAIL)
                .on(Tables.EMAIL.PERSON_ID.eq(Tables.PERSON.ID))
                .join(Tables.USER_AUTH)
                .on(Tables.USER_AUTH.ID.eq(Tables.PERSON.USER_ID))
                .where(Tables.USER_AUTH.LOGIN.ne(login)
                        .and(searchCondition))
                .stream().map(Record1::component1)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> getPersonDtoByPersonId(
            List<Long> personIds,
            Pageable pageable) {
        return dsl
                .selectFrom(Tables.PERSON)
                .where(Tables.PERSON.ID.in(personIds))
                .orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .fetchInto(PersonDto.class);
    }

    private Condition getConditions(String birthDate,
                                    String phoneNumber,
                                    String firstName,
                                    String lastName,
                                    String surName,
                                    String emailAddress) {

        Condition condition = trueCondition();
        try {
            if (!birthDate.isEmpty()) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(birthDate);
                OffsetDateTime offsetDateTime = date.toInstant()
                        .atOffset(ZoneOffset.UTC);
                condition = condition.and(Tables.PERSON.BIRTH_DATE
                        .ge(offsetDateTime));
            }

        } catch (ParseException ex) {
            log.debug(ex.getMessage());
        }

        if (!phoneNumber.isEmpty()) {
            condition = condition.and(Tables.PHONE.PHONE_NUMBER.equal(phoneNumber));
        }

        if (!firstName.isEmpty()) {
            condition = condition.and(Tables.PERSON.FIRST_NAME.equalIgnoreCase(firstName));
        }

        if (!lastName.isEmpty()) {
            condition = condition.and(Tables.PERSON.LAST_NAME.equalIgnoreCase(lastName));
        }

        if (!surName.isEmpty()) {
            condition = condition.and(Tables.PERSON.SUR_NAME.equalIgnoreCase(surName));
        }

        if (!emailAddress.isEmpty()) {
            condition = condition.and(Tables.EMAIL.EMAIL_ADDRESS.equal(emailAddress));
        }

        return condition;
    }

    private Collection<SortField<?>> getSortFields(Sort sortSpecification) {
        Collection<SortField<?>> querySortFields = new ArrayList<>();

        if (sortSpecification == null) {
            return querySortFields;
        }

        Iterator<Sort.Order> specifiedFields = sortSpecification.iterator();

        while (specifiedFields.hasNext()) {
            Sort.Order specifiedField = specifiedFields.next();

            String sortFieldName = specifiedField.getProperty();
            Sort.Direction sortDirection = specifiedField.getDirection();

            TableField tableField = getTableField(sortFieldName);
            SortField<?> querySortField = convertTableFieldToSortField(tableField, sortDirection);
            querySortFields.add(querySortField);
        }

        return querySortFields;
    }

    private TableField getTableField(String sortFieldName) {
        TableField sortField = null;
        try {
            Field tableField = Tables.PERSON.getClass().getField(sortFieldName);

            sortField = (TableField) tableField.get(Tables.PERSON);

        } catch (NoSuchFieldException | IllegalAccessException ex) {
            String errorMessage = String.format("Could not find table field: {}", sortFieldName);
            throw new InvalidDataAccessApiUsageException(errorMessage, ex);
        }

        return sortField;
    }

    private SortField<?> convertTableFieldToSortField(TableField tableField, Sort.Direction sortDirection) {
        if (sortDirection == Sort.Direction.ASC) {
            return tableField.asc();
        } else {
            return tableField.desc();
        }
    }
}
