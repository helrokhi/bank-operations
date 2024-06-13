package ru.bankoperations.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "databaseClient", dismiss404 = true, url = "${database.url}" + "/deposit")
public interface DatabaseClient {
    @GetMapping("")
    List<Long> findAllPersonId();

    @PutMapping("/{id}")
    ResponseEntity<Void> updateDeposit(@PathVariable Long id);
}
