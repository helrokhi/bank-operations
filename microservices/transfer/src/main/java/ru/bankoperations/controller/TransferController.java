package ru.bankoperations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.dto.*;
import ru.bankoperations.service.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/beneficiaries")
public class TransferController {
    private final TransferService transferService;

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
        return ResponseEntity.ok(transferService.searchPerson(
                birthDate,
                phoneNumber,
                firstName,
                lastName,
                surName,
                emailAddress,
                login,
                page)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonsById(
            @PathVariable Long id,
            @RequestParam String login) {
        return transferService.getPersonsById(id, login);
    }

    @PutMapping("/transfer/{id}")
    public ResponseEntity<Void> transfer(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0.00") String amount,
            @RequestParam String login) {

        return transferService.transfer(id, amount, login);
    }
}
