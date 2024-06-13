package ru.bankoperations.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.dto.*;

@FeignClient(name = "profileClient", dismiss404 = true, url = "${profile.url}" + "/api/v1")
public interface ProfileClient {

    @PostMapping("/auth/register")
    ResponseEntity<Void> registrationPerson(@RequestBody RegistrationDto registrationDto);

    @PostMapping("/auth/password/recovery/{token}")
    ResponseEntity<Void> resetForgotPassword(
            @PathVariable("token") String token, @RequestParam("password") String password);

    @GetMapping("/account/me")
    ResponseEntity<PersonDto> getMyProfile(@RequestParam("login") String login);

    @PutMapping("/account/me")
    ResponseEntity<PersonDto> updateMyProfile(
            @RequestBody PersonDto dto,
            @RequestParam("login") String login);

    @GetMapping("/account/{id}")
    ResponseEntity<PersonDto> getProfileById(@PathVariable Long id);
}
