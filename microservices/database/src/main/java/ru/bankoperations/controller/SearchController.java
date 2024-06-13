package ru.bankoperations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bankoperations.dto.PersonDto;
import ru.bankoperations.service.SearchService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/beneficiaries")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<List<PersonDto>> searchPerson(
            @RequestParam(defaultValue = "") String birthDate,
            @RequestParam(defaultValue = "") String phoneNumber,
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "") String surName,
            @RequestParam(defaultValue = "") String emailAddress,
            @RequestParam String login,
            Pageable page) {
        log.info("all beneficiaries login {}", login);

        logInfoSearchPerson(birthDate,
                phoneNumber,
                firstName, lastName, surName,
                emailAddress,
                login, page);

        List<PersonDto> result = searchService.searchPerson(
                birthDate,
                phoneNumber,
                firstName,
                lastName,
                surName,
                emailAddress,
                login,
                page);

        return ResponseEntity.ok(result);
    }

    private void logInfoSearchPerson(
            String birthDate,
            String phoneNumber,
            String firstName,
            String lastName,
            String surName,
            String emailAddress,
            String login,
            Pageable pageable) {
        log.info("getAllPerson");
        log.debug("birthDate: {}, phoneNumber: {}," +
                        "firstName: {},lastName: {}, " +
                        "surName: {}, emailAddress: {}, " +
                        "login: {}, pageable {}",
                birthDate,
                phoneNumber,
                firstName,
                lastName,
                surName,
                emailAddress,
                login,
                pageable);
    }
}
