package ru.bankoperations.repository;

import jooq.db.tables.records.*;
import ru.bankoperations.dto.*;

import java.util.Optional;

/**
 * репозиторий для работы с таблицей user_auth
 */
public interface UserAuthRepository {
    Optional<UserAuthDto> getUserByLogin(String login);

    int getUsersCountByLogin(String login);

    Optional<UserAuthRecord> insertUserAuth(UserAuthRecord userAuthRecord);

    Optional<PersonDto> getAccountInfo(String login);

    void deleteUserAuthById(Long id);
}
