package ru.bankoperations.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.client.DatabaseClient;
import ru.bankoperations.dto.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final DatabaseClient databaseClient;

    @GetMapping("/me")
    public ResponseEntity<PersonDto> getMyProfile(@RequestParam("login") String login) {
        return databaseClient.getAccountInfo(login);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getProfileById(@PathVariable Long id) {
        return databaseClient.getProfileById(id);
    }

    @PutMapping("/me")
    public ResponseEntity<PersonDto> updateMyProfile(@RequestBody PersonDto dto, @RequestParam("login") String login) {
        return databaseClient.updateMyProfile(dto, login);
    }
}
