package ru.bankoperations.service;

import jooq.db.tables.records.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.bankoperations.dto.*;

import java.util.Optional;

/**
 * сервис для работы с таблицей user_auth
 */
public interface UserAuthService {
    Optional<UserAuthRecord> createUserAuth(RegDtoDb regDtoDb);

    Optional<UserAuthDto> getUserByLogin(String login);

    int getUsersCountByLogin(String login);

    Optional<PersonDto> getAccountInfo(String login);

    Optional<PersonDto> updateAccount(PersonDto dto, String login);

    Optional<PersonDto> getAccountById(Long id);

    void deleteUserAuthById(Long id);
}
