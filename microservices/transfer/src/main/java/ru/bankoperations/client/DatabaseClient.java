package ru.bankoperations.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.dto.*;

import java.util.List;

@FeignClient(name = "databaseClient", dismiss404 = true, url = "${database.url}" + "/beneficiaries")
public interface DatabaseClient {
    @GetMapping("")
    List<PersonDto> searchPerson(
            @RequestParam(defaultValue = "") String birthDate,
            @RequestParam(defaultValue = "") String phoneNumber,
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "") String surName,
            @RequestParam(defaultValue = "") String emailAddress,
            @RequestParam String login,
            Pageable page);

    @GetMapping("/{id}")
    ResponseEntity<PersonDto> getPersonsById(
            @PathVariable Long id,
            @RequestParam String login);

    @PutMapping("/transfer/{id}")
    ResponseEntity<Void> transfer(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0.00") String amount,
            @RequestParam String login);
}
