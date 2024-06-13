package ru.bankoperations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.dto.*;
import ru.bankoperations.service.*;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
    private final UserAuthService userAuthService;

    @GetMapping("/me")
    public ResponseEntity<PersonDto> getAccountInfo(@RequestParam("login") String login) {
        Optional<PersonDto> accountInfo = userAuthService.getAccountInfo(login);
        return accountInfo
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getAccountById(@PathVariable Long id) {
        Optional<PersonDto> accountInfo = userAuthService.getAccountById(id);
        return accountInfo
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<PersonDto> updateAccount(
            @RequestBody PersonDto dto,
            @RequestParam("login") String login) {
        Optional<PersonDto> accountInfo = userAuthService.updateAccount(dto, login);
        return accountInfo
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
