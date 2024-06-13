package ru.bankoperations.repository;

import jooq.db.Tables;
import jooq.db.tables.records.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.stereotype.Repository;
import ru.bankoperations.dto.*;
import ru.bankoperations.repository.mapper.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Slf4j
public class PersonRepository implements UserInteraction, PersonIteraction {

    private final DSLContext dsl;
    private final PersonDtoPersonRecordMapping mapper;

    public Optional<PersonRecord> insertPerson(PersonRecord personRecord) {
        return dsl.insertInto(Tables.PERSON)
                .set(personRecord)
                .returning()
                .fetchOptional();
    }

    public Optional<PersonRecord> updatePerson(PersonRecord record, String login) {

        Long id = findUserIdByLogin(login);

        Optional<PersonRecord> optionalDbRecord = dsl.selectFrom(Tables.PERSON)
                .where(Tables.PERSON.USER_ID.eq(id))
                .fetchOptional();

        if (optionalDbRecord.isPresent()) {
            PersonRecord dbRecord = optionalDbRecord.get();
            fillAccount(record, dbRecord);

            return dsl.update(Tables.PERSON)
                    .set(dsl.newRecord(Tables.PERSON, record))
                    .where(Tables.PERSON.USER_ID.eq(id))
                    .returning()
                    .fetchOptional();
        }
        return Optional.empty();
    }

    public Long findUserIdByLogin(String login) {
        return dsl.selectFrom(Tables.USER_AUTH)
                .where(Tables.USER_AUTH.LOGIN.eq(login))
                .fetchOptional()
                .map(UserAuthRecord::getId)
                .orElse(0L);
    }

    public Long getPersonIdByLogin(String login) {
        return dsl.selectFrom(Tables.PERSON)
                .where(Tables.PERSON.USER_ID.eq(
                        dsl.select(Tables.USER_AUTH.ID)
                                .from(Tables.USER_AUTH)
                                .where(Tables.USER_AUTH.LOGIN.eq(login))
                ))
                .fetchOptional()
                .map(PersonRecord::getId)
                .orElse(null);
    }

    public Optional<PersonRecord> getPersonById(Long id) {
        return dsl.selectFrom(Tables.PERSON)
                .where(Tables.PERSON.ID.eq(id))
                .fetchOptional();

    }

    @Override
    public List<Long> findAllPersonId() {
        return dsl
                .select(Tables.PERSON.ID)
                .from(Tables.PERSON)
                .fetch()
                .map(Record1::component1);
    }

    public PersonDto getPersonDtoById(Long personId) {
        return dsl
                .selectFrom(Tables.PERSON)
                .where(Tables.PERSON.ID.eq(personId))
                .fetchOptional()
                .map(mapper::personRecordToPersonDto)
                .orElse(null);
    }

    public void deletePerson(Long personId) {
        int result = dsl
                .deleteFrom(Tables.PERSON)
                .where(Tables.PERSON.ID.eq(personId))
                .execute();
    }

    public Optional<PersonRecord> updateBalance(Long personId, String balance) {
        return dsl
                .update(Tables.PERSON)
                .set(Tables.PERSON.BALANCE, balance)
                .where(Tables.PERSON.ID.eq(personId))
                .returning()
                .fetchOptional();
    }

    private void fillAccount(PersonRecord dest, PersonRecord src) {
//        dest.setRegDate(src.getRegDate());
//        dest.setStatusCode(src.getStatusCode());
//        dest.setMessagesPermission(src.getMessagesPermission());
//        dest.setIsBlocked(src.getIsBlocked());
//        dest.setCreatedDate(src.getCreatedDate());
//        dest.setLastOnlineTime(OffsetDateTime.now());
//        dest.setIsDeleted(src.getIsDeleted());
//        dest.setIsOnline(true);
//        dest.setLastModifiedDate(OffsetDateTime.now());
    }
}
