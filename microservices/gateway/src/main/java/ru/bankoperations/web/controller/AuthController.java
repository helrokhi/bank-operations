package ru.bankoperations.web.controller;

import feign.FeignException;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.client.ProfileClient;
import ru.bankoperations.dto.*;
import ru.bankoperations.security.SecurityService;
import ru.bankoperations.service.UserService;
import ru.bankoperations.web.model.*;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final SecurityService securityService;
    private final ProfileClient profileClient;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private AtomicInteger usersCount;

    public AuthController(
            SecurityService securityService,
            ProfileClient profileClient,
            PasswordEncoder passwordEncoder,
            UserService userService,
            MeterRegistry meterRegistry) {
        this.securityService = securityService;
        this.profileClient = profileClient;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        meterRegistry.gauge("users_count", usersCount);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> signIn(@RequestBody AuthRequest authRequest) {
        log.debug("/api/v1/auth/login");
        log.debug(authRequest.getLogin());

        JwtResponse responseBody = securityService.authenticateUser(authRequest);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        log.debug("/api/v1/auth/refresh");
        log.debug(refreshRequest.toString());

        try {
            JwtResponse responseBody = securityService.refreshToken(refreshRequest);
            return ResponseEntity.ok(responseBody);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logout(@AuthenticationPrincipal UserDetails userDetails) {
        log.debug("/api/v1/auth/logout");
        log.debug(userDetails.getUsername());

        SimpleResponse responseBody = securityService.logout(userDetails.getUsername());

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registrationPerson(@RequestBody RegistrationDto registrationDto) {
        log.debug("/api/v1/auth/register");
        log.debug(registrationDto.toString());

        if (securityService.doPasswordsMatch(registrationDto)) {
            ResponseEntity<Void> inputResponseEntity =
                    profileClient.registrationPerson(
                            securityService.getRegDtoWithEncryptedPassword(registrationDto));
            HttpStatusCode statusCode = inputResponseEntity.getStatusCode();

            if (statusCode.isSameCodeAs(HttpStatusCode.valueOf(404))) {
                return ResponseEntity.badRequest().build();
            }

            return inputResponseEntity;
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler(FeignException.class)
    private ResponseEntity<Void> handler(FeignException ex) {
        log.warn("Error in the gateway {}", ex.getMessage());
        return ResponseEntity.status(ex.status()).build();
    }
}
