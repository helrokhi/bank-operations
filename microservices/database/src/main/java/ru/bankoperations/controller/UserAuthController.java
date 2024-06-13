package ru.bankoperations.controller;

import jooq.db.tables.records.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankoperations.dto.*;
import ru.bankoperations.service.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/auth")
public class UserAuthController {
    private final RegistrationService registrationService;
    private final UserAuthService userAuthService;
    private final PhoneService phoneService;
    private final EmailService emailService;
    private final DepositService depositService;

    @PostMapping("/register/create")
    public ResponseEntity<PersonDto> createPerson(@RequestBody RegDtoDb regDtoDb) {
        log.info("create person: regDtoDb {}", regDtoDb);

        boolean isSuccessful = false;

        Optional<UserAuthRecord> userAuthRecord = userAuthService.createUserAuth(regDtoDb);

        if (userAuthRecord.isPresent()) {
            Long userId = userAuthRecord.get().getId();
            log.info("create person: userId {}", userId);
            try {
                Optional<PersonRecord> personRecord = registrationService.createPerson(regDtoDb, userId);

                if (personRecord.isPresent()) {
                    Long personId = personRecord.get().getId();
                    log.info("create person: personId {}", personId);
                    Optional<PhoneRecord> phoneRecord = phoneService.createPhone(personId, regDtoDb);
                    log.info("create person: phoneRecord.isPresent() {}", phoneRecord.isPresent());
                    Optional<EmailRecord> emailRecord = emailService.createEmail(personId, regDtoDb);
                    log.info("create person: emailRecord.isPresent() {}", emailRecord.isPresent());
                    Optional<DepositRecord> depositRecord = depositService.createDeposit(personId, regDtoDb);
                    log.info("create person: depositRecord.isPresent() {}", depositRecord.isPresent());

                    isSuccessful = phoneRecord.isPresent() &
                            emailRecord.isPresent() &
                            depositRecord.isPresent()
                    ;

                    if (!isSuccessful) {
                        registrationService.deletePersonById(personId);
                    }
                }

            } catch (Exception ex) {
                log.debug(ex.getMessage());
            }

            if (!isSuccessful) {
                userAuthService.deleteUserAuthById(userId);
            }
        }

        return isSuccessful ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/register/login")
    public ResponseEntity<Integer> getCountPersonByLogin(@RequestParam("login") String login) {
        int count = userAuthService.getUsersCountByLogin(login);
        return count > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/register/phone")
    public ResponseEntity<Integer> getCountPersonByPhone(@RequestParam("phone") String phone) {
        int count = phoneService.getPhoneByPhoneNumber(phone);
        return count > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/register/email")
    public ResponseEntity<Integer> getCountPersonByEmail(@RequestParam("email") String email) {
        int count = emailService.getEmailByEmailAddress(email);
        return count > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/user")
    public ResponseEntity<UserAuthDto> getUser(@RequestParam("login") String login) {
        Optional<UserAuthDto> user = userAuthService.getUserByLogin(login);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
