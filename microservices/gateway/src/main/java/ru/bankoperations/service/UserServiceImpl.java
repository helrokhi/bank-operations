package ru.bankoperations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bankoperations.client.DatabaseClient;
import ru.bankoperations.dto.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final DatabaseClient databaseClient;

    @Override
    public Optional<UserAuthDto> findUserByLogin(String login) {
        return Optional.ofNullable(databaseClient.getUserByLogin(login));
    }
}
