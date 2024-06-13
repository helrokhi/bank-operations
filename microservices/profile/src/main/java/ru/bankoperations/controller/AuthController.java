package ru.bankoperations.controller;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.dto.*;
import ru.bankoperations.service.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<Void> registrationPerson(@RequestBody RegistrationDto registrationDto) {
        boolean result = registrationService.registrationPerson(registrationDto);
        return result ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(FeignException.class)
    private ResponseEntity<Void> handler(FeignException ex) {
        log.warn("Error in profile {}", ex.getMessage());
        return ResponseEntity.status(ex.status()).build();
    }
}
