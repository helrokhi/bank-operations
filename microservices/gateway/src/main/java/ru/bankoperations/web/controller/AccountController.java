package ru.bankoperations.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.client.*;
import ru.bankoperations.dto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final ProfileClient profileClient;

    @GetMapping("/me")
    public ResponseEntity<PersonDto> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("/api/v1/account/me");
        if (userDetails != null) {
            log.info(userDetails.getUsername());
            return profileClient.getMyProfile(userDetails.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/me")
    public ResponseEntity<PersonDto> updateMyProfile(
            @RequestBody PersonDto personDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        log.info("update person with login: {}", login);
        ResponseEntity<PersonDto> inputResponseEntity = profileClient.updateMyProfile(personDto, login);
        HttpStatusCode statusCode = inputResponseEntity.getStatusCode();
        if (statusCode.isSameCodeAs(HttpStatusCode.valueOf(404))) {
            return ResponseEntity.badRequest().build();
        }

        return inputResponseEntity;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getProfileById(@PathVariable Long id) {
        log.info("get profile by id: {}", id);
        ResponseEntity<PersonDto> inputResponseEntity = profileClient.getProfileById(id);
        HttpStatusCode statusCode = inputResponseEntity.getStatusCode();

        if (statusCode.isSameCodeAs(HttpStatusCode.valueOf(404))) {
            return ResponseEntity.badRequest().build();
        }
        return inputResponseEntity;
    }
}
