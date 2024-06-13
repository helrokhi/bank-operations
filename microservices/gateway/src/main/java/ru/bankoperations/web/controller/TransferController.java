package ru.bankoperations.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.client.TransferClient;
import ru.bankoperations.dto.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/beneficiaries")
public class TransferController {
    private final TransferClient transferClient;

    @GetMapping("")
    public ResponseEntity<?> searchAccounts(
            @RequestParam(defaultValue = "") String birthDate,
            @RequestParam(defaultValue = "") String phoneNumber,
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "") String surName,
            @RequestParam(defaultValue = "") String emailAddress,
            @RequestParam(defaultValue = "0") String size,
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {

        if (userDetails == null) {
            log.info("/api/v1/beneficiaries -> userDetails is null.");
            return ResponseEntity.badRequest().build();
        }

        String login = userDetails.getUsername();
        logInfoSearchPerson(birthDate,
                phoneNumber,
                firstName, lastName, surName,
                emailAddress,
                login, size, pageable);

        if (birthDate.equals("")
                && phoneNumber.equals("")
                && firstName.equals("")
                && lastName.equals("")
                && surName.equals("")
                && emailAddress.equals("")
                && !size.equals("0")) {

            return ResponseEntity.badRequest().build();
        }

        ResponseEntity<List<PersonDto>> inputResponseEntity =
                transferClient.searchPerson(
                        birthDate,
                        phoneNumber,
                        firstName,
                        lastName,
                        surName,
                        emailAddress,
                        login,
                        pageable);

        return ResponseEntity.ok(
                new PageImpl<>(inputResponseEntity.getBody(),
                        pageable,
                        inputResponseEntity.getBody().size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getProfileById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("get profile by id: {}", id);

        if (userDetails == null) {
            log.info("/api/v1/beneficiaries -> userDetails is null.");
            return ResponseEntity.badRequest().build();
        }

        String login = userDetails.getUsername();
        ResponseEntity<PersonDto> inputResponseEntity = transferClient.getPersonsById(id, login);
        HttpStatusCode statusCode = inputResponseEntity.getStatusCode();

        if (statusCode.isSameCodeAs(HttpStatusCode.valueOf(404))) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(inputResponseEntity.getStatusCode())
                .body(inputResponseEntity.getBody());
    }

    @PutMapping("/transfer/{id}")
    public ResponseEntity<Void> transfer(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0.00") String amount,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            log.info("/api/v1/beneficiaries/transfer/{} -> userDetails is null.", id);
            return ResponseEntity.badRequest().build();
        }

        String login = userDetails.getUsername();
        log.info("/api/v1/beneficiaries/transfer/{} -> amount - {}, login - {}", id, amount, login);
        transferClient.transfer(id, amount, login);
        return ResponseEntity.ok().build();
    }

    private void logInfoSearchPerson(
            String birthDate,
            String phoneNumber,
            String firstName,
            String lastName,
            String surName,
            String emailAddress,
            String login,
            String size,
            Pageable pageable) {
        log.info("getAllPerson");
        log.debug("""

                        birthDate: {},
                        phoneNumber: {},
                        firstName: {},
                        lastName: {},
                        surName: {},
                        emailAddress: {},
                        login: {},
                        size: {},
                        pageable {}""",
                birthDate,
                phoneNumber,
                firstName,
                lastName,
                surName,
                emailAddress,
                login,
                size,
                pageable);
    }
}
