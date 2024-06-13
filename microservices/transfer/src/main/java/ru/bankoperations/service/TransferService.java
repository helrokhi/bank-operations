package ru.bankoperations.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.bankoperations.dto.*;

import java.util.List;

public interface TransferService {
    List<PersonDto> searchPerson(
            String birthDate,
            String phoneNumber,
            String firstName,
            String lastName,
            String surName,
            String emailAddress,
            String login,
            Pageable page);

    ResponseEntity<PersonDto> getPersonsById(Long id, String login);

    ResponseEntity<Void> transfer(Long id, String amount, String login);
}
