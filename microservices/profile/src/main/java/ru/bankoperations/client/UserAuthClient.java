package ru.bankoperations.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bankoperations.dto.*;

@FeignClient(name = "userAuth", dismiss404 = true, url = "${database.url}" + "/auth")
public interface UserAuthClient {

    @GetMapping("auth/user")
    UserAuthDto getUserByLogin(@RequestParam("login") String login);

    @GetMapping("/register/login")
    ResponseEntity<Void> isLoginExist(@RequestParam("login") String login);

    @PostMapping("/register/create")
    ResponseEntity<Void> createPerson(PersonDto personDto);
}
