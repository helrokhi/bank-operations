package ru.bankoperations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bankoperations.client.DatabaseClient;
import ru.bankoperations.dto.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final DatabaseClient client;

    //private static final String EMAIL_REGEX = "^(.+)@(\\S+)$";
    //private static final String PHONE_REGEX = "";

    @Override
    public List<PersonDto> searchPerson(
            String birthDate, String phoneNumber,
            String firstName, String lastName, String surName,
            String emailAddress, String login, Pageable page) {
        return client.searchPerson(
                birthDate,
                phoneNumber,
                firstName,
                lastName,
                surName,
                emailAddress,
                login,
                page);
    }

    @Override
    public ResponseEntity<PersonDto> getPersonsById(Long id, String login) {
        return client.getPersonsById(id, login);
    }

    @Override
    public ResponseEntity<Void> transfer(Long id, String amount, String login) {
        return client.transfer(id, amount, login);
    }
}
