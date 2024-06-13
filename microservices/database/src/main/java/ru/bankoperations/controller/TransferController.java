package ru.bankoperations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.dto.*;
import ru.bankoperations.service.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/beneficiaries")
public class TransferController {
    private final TransferService transferService;

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonsById(
            @PathVariable Long id,
            @RequestParam String login) {
        log.info("get beneficiary login - {},  id - {}", login, id);

        try {
            PersonDto personDto = transferService.getPersonDtoById(id, login);
            return ResponseEntity.ok(personDto);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/transfer/{id}")
    public ResponseEntity<Void> transfer(
            @PathVariable Long id,
            @RequestParam (defaultValue="0.00") String amount,
            @RequestParam String login) {
        log.info("put beneficiary id - {}, transfer login - {}, amount -{}", id, login, amount);

        try {
            transferService.isTransfer(login, id, amount);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
