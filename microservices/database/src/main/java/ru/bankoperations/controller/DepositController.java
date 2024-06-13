package ru.bankoperations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.service.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/deposit")
public class DepositController {
    private final DepositService depositService;

    @GetMapping("")
    public List<Long> findAllPersonId(){
        return depositService.findAllPersonId();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDeposit(@PathVariable Long id) {
        log.info("update all balance and deposit");
        try {
            depositService.updateDeposit(id);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
