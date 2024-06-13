package ru.bankoperations.service;

import ru.bankoperations.dto.RegistrationDto;

public interface RegistrationService {

    boolean registrationPerson(RegistrationDto registrationDto);
}
