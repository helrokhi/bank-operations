package ru.bankoperations.repository;

import org.springframework.data.domain.Pageable;
import ru.bankoperations.dto.*;

import java.time.OffsetDateTime;
import java.util.List;

public interface SearchRepository {
    List<Long> searchPerson(String birthDate, String phoneNumber, String firstName, String lastName, String surName, String emailAddress, String login);

    List<PersonDto> getPersonDtoByPersonId(List<Long> personId, Pageable page);
}
