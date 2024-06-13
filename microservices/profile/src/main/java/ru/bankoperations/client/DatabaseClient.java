package ru.bankoperations.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.dto.*;

@FeignClient(name = "databaseClient", dismiss404 = true, url = "${database.url}")
public interface DatabaseClient {

    @GetMapping("/auth/user")
    UserAuthDto getUserByLogin(@RequestParam("login") String login);

    @GetMapping("/auth/register/login")
    ResponseEntity<Void> isLoginExist(@RequestParam("login") String login);

    @GetMapping("/auth/register/phone")
    ResponseEntity<Void> isPhoneNumberExist(@RequestParam("phone") String phone);

    @GetMapping("/auth/register/email")
    ResponseEntity<Void> isEmailAddressExist(@RequestParam("email") String email);

    @PostMapping("/auth/register/create")
    ResponseEntity<Void> createPerson(RegDtoDb regDtoDb);

    @GetMapping("/account/me")
    ResponseEntity<PersonDto> getAccountInfo(@RequestParam("login") String login);

    @PutMapping("/account/me")
    ResponseEntity<PersonDto> updateMyProfile(
            @RequestBody PersonDto personDto,
            @RequestParam("login") String login);

    @GetMapping("/account/{id}")
    ResponseEntity<PersonDto> getProfileById(@PathVariable Long id);
}
