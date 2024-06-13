package ru.bankoperations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import ru.bankoperations.client.DatabaseClient;
import ru.bankoperations.dto.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    private final DatabaseClient databaseClient;
    private final static String REG_PROCESS = "Registration process: ";

    @Override
    public boolean registrationPerson(RegistrationDto registrationDto) {
        String login = registrationDto.getLogin();
        String phoneNumber = registrationDto.getPhoneNumber();
        String emailAddress = registrationDto.getEmailAddress();

        if (login == null) {
            log.info(REG_PROCESS + "login is null");
            return false;
        }

        if (phoneNumber == null) {
            log.info(REG_PROCESS + "phone number is null");
            return false;
        }

        if (emailAddress == null) {
            log.info(REG_PROCESS + "email address is null");
            return false;
        }

        boolean isCreatePerson = !isLoginExisted(login) &
                !isPhoneNumberExisted(phoneNumber) &
                !isEmailAddressExisted(emailAddress);

        if (isCreatePerson) {
            RegDtoDb regDtoForDb = createRegDtoForDb(registrationDto);
            return createPerson(regDtoForDb);
        }

        return false;
    }

    private boolean isLoginExisted(String login) {
        HttpStatusCode statusCode = databaseClient.isLoginExist(login).getStatusCode();

        if (statusCode.is2xxSuccessful()) {
            log.info(REG_PROCESS + "Person with login ({}) has already existed," +
                    "registration failed", login);
            return true;
        }
        log.info(REG_PROCESS + "login is unique");
        return false;
    }

    private boolean isPhoneNumberExisted(String phoneNumber) {
        HttpStatusCode statusCode = databaseClient.isPhoneNumberExist(phoneNumber).getStatusCode();

        if (statusCode.is2xxSuccessful()) {
            log.info(REG_PROCESS + "Person with phoneNumber ({}) has already existed," +
                    "registration failed", phoneNumber);
            return true;
        }
        log.info(REG_PROCESS + "phoneNumber is unique");
        return false;
    }

    private boolean isEmailAddressExisted(String emailAddress) {
        HttpStatusCode statusCode = databaseClient.isEmailAddressExist(emailAddress).getStatusCode();

        if (statusCode.is2xxSuccessful()) {
            log.info(REG_PROCESS + "Person with email address ({}) has already existed," +
                    "registration failed", emailAddress);
            return true;
        }
        log.info(REG_PROCESS + "email address is unique");
        return false;
    }

    private boolean createPerson(RegDtoDb regDtoDb) {
        HttpStatusCode statusCode = databaseClient.createPerson(regDtoDb).getStatusCode();
        if (statusCode.is2xxSuccessful()) {
            log.info(REG_PROCESS + "Person has been registered");
            return true;
        }
        log.info(REG_PROCESS + "Person has not been registered, database does not answer");
        return false;
    }

    private RegDtoDb createRegDtoForDb(RegistrationDto registrationDto) {
        return RegDtoDb.builder()
                .login(registrationDto.getLogin())
                .password(registrationDto.getPassword1())
                .balance(registrationDto.getBalance())
                .phoneNumber(registrationDto.getPhoneNumber())
                .emailAddress(registrationDto.getEmailAddress())
                .build();
    }
}
