package ru.bankoperations.service;

import ru.bankoperations.dto.*;

import java.util.Optional;

public interface UserService {
    Optional<UserAuthDto> findUserByLogin(String login);
}
