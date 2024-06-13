package ru.bankoperations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.bankoperations.dto.*;
import ru.bankoperations.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final SearchRepository searchRepository;

    public List<PersonDto> searchPerson(
            String birthDate,
            String phoneNumber,
            String firstName,
            String lastName,
            String surName,
            String emailAddress,
            String login,
            Pageable page) {

        List<Long> personIds = searchRepository.searchPerson(
                birthDate,
                phoneNumber,
                firstName,
                lastName,
                surName,
                emailAddress,
                login);

        log.info("search login {} person personIds {}", login, personIds);

        return searchRepository.getPersonDtoByPersonId(personIds, page);
    }
}
