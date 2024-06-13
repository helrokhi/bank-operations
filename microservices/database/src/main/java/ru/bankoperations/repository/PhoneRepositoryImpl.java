package ru.bankoperations.repository;

import jooq.db.Tables;
import jooq.db.tables.records.*;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.bankoperations.dto.*;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PhoneRepositoryImpl implements PhoneRepository {
    private final DSLContext dsl;
    @Override
    public List<PhoneDto> findAllPhoneNumbersByPersonId(Long personId) {
        return dsl
                .selectFrom(Tables.PHONE)
                .where(Tables.PHONE.PERSON_ID.eq(personId))
                .fetchInto(PhoneDto.class);
    }

    @Override
    public int addNewPhoneNumber(PhoneRecord phoneRecord) {
        return dsl
                .insertInto(Tables.PHONE)
                .set(phoneRecord)
                .execute();
    }

    @Override
    public int deletePhoneNumber(Long phoneId) {
        return dsl
                .deleteFrom(Tables.PHONE)
                .where(Tables.PHONE.ID.eq(phoneId))
                .execute();
    }

    @Override
    public PhoneDto getPhoneDtoById(Long phoneId) {
        return dsl
                .selectFrom(Tables.PHONE)
                .where(Tables.PHONE.ID.eq(phoneId))
                .fetchOptional()
                .map(phoneRecord->PhoneDto.builder().build())
                .orElse(null);
    }

    @Override
    public Optional<PhoneRecord> insertPhone(PhoneRecord phoneRecord) {
        return dsl.insertInto(Tables.PHONE)
                .set(phoneRecord)
                .returning()
                .fetchOptional();
    }

    @Override
    public int getPhoneByPhoneNumber(String phoneNumber) {
        return dsl
                .selectFrom(Tables.PHONE)
                .where(Tables.PHONE.PHONE_NUMBER.eq(phoneNumber))
                .execute();
    }
}
