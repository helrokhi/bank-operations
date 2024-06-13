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
public class EmailRepositoryImpl implements EmailRepository{
    private final DSLContext dsl;

    @Override
    public List<EmailDto> findAllEmailAddressesByPersonId(Long personId) {
        return dsl
                .selectFrom(Tables.EMAIL)
                .where(Tables.EMAIL.PERSON_ID.eq(personId))
                .fetchInto(EmailDto.class);
    }

    @Override
    public int addNewEmailAddress(EmailRecord emailRecord) {
        return dsl
                .insertInto(Tables.EMAIL)
                .set(emailRecord)
                .execute();
    }

    @Override
    public int deleteEmailAddress(Long emailId) {
        return dsl
                .deleteFrom(Tables.EMAIL)
                .where(Tables.EMAIL.ID.eq(emailId))
                .execute();
    }

    @Override
    public EmailDto getEmailDtoById(Long emailId) {
        return dsl
                .selectFrom(Tables.EMAIL)
                .where(Tables.EMAIL.ID.eq(emailId))
                .fetchOptional()
                .map(emailRecord->EmailDto.builder().build())
                .orElse(null);
    }

    @Override
    public Optional<EmailRecord> insertEmail(EmailRecord emailRecord) {
        return dsl.insertInto(Tables.EMAIL)
                .set(emailRecord)
                .returning()
                .fetchOptional();
    }

    @Override
    public int getEmailByEmailAddress(String emailAddress) {
        return dsl
                .selectFrom(Tables.EMAIL)
                .where(Tables.EMAIL.EMAIL_ADDRESS.eq(emailAddress))
                .execute();
    }
}
