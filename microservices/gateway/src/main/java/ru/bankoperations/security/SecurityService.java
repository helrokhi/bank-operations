package ru.bankoperations.security;

import ru.bankoperations.dto.RegistrationDto;
import ru.bankoperations.exception.RefreshTokenException;
import ru.bankoperations.web.model.*;

public interface SecurityService {
    JwtResponse authenticateUser(AuthRequest authRequest);

    JwtResponse refreshToken(RefreshRequest refreshRequest) throws RefreshTokenException;

    SimpleResponse logout(String userName);

    boolean doPasswordsMatch(RegistrationDto registrationDto);

    RegistrationDto getRegDtoWithEncryptedPassword(RegistrationDto inputDto);
}
