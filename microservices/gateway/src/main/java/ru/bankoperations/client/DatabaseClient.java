package ru.bankoperations.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bankoperations.dto.UserAuthDto;

@FeignClient(name = "databaseClient", dismiss404 = true, url = "${database.url}" + "/auth")
public interface DatabaseClient {
    @GetMapping("/user")
    UserAuthDto getUserByLogin(@RequestParam("login") String login);
}

