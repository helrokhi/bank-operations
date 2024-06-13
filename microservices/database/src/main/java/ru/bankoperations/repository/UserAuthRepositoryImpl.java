package ru.bankoperations.repository;

import jooq.db.Tables;
import jooq.db.tables.records.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.springframework.stereotype.Repository;
import ru.bankoperations.dto.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserAuthRepositoryImpl implements UserAuthRepository {
    private final DSLContext dsl;

    @Override
    public Optional<UserAuthDto> getUserByLogin(String login) {
        List<UserAuthDto> userList =
                dsl
                        .selectFrom(Tables.USER_AUTH)
                        .where(Tables.USER_AUTH.LOGIN.eq(login))
                        .fetchInto(UserAuthDto.class);

        UserAuthDto user = (!userList.isEmpty()) ? userList.get(0) : null;

        return Optional.ofNullable(user);
    }

    @Override
    public int getUsersCountByLogin(String login) {
        return dsl
                .selectFrom(Tables.USER_AUTH)
                .where(Tables.USER_AUTH.LOGIN.eq(login))
                .execute();
    }

    @Override
    public Optional<UserAuthRecord> insertUserAuth(UserAuthRecord userAuthRecord) {
        return dsl
                .insertInto(Tables.USER_AUTH)
                .set(userAuthRecord)
                .returning()
                .fetchOptional();
    }

    @Override
    public Optional<PersonDto> getAccountInfo(String login) {
        return dsl
                .select()
                .from(
                        Tables.USER_AUTH
                                .leftOuterJoin(Tables.PERSON)
                                .on(Tables.USER_AUTH.ID.eq(Tables.PERSON.USER_ID)))
                .where(Tables.USER_AUTH.LOGIN.eq(login))
                .fetchOptionalInto(PersonDto.class);
    }

    @Override
    public void deleteUserAuthById(Long id) {
        int result =
                dsl.deleteFrom(Tables.USER_AUTH).where(Tables.USER_AUTH.ID.eq(id)).execute();
    }
}
