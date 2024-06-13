package ru.bankoperations.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.bankoperations.dto.RegistrationDto;
import ru.bankoperations.exception.RefreshTokenException;
import ru.bankoperations.security.jwt.JwtUtils;
import ru.bankoperations.web.model.*;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityServiceImpl implements SecurityService {
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final Map<String, String> refreshTokenCache = new HashMap<>();

    @Override
    public JwtResponse authenticateUser(AuthRequest authRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.getLogin(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        return generateJwtResponse(userDetails);
    }

    @Override
    public JwtResponse refreshToken(RefreshRequest refreshRequest) throws RefreshTokenException {
        String refreshToken = refreshRequest.getRefreshToken();

        try {
            if (StringUtils.hasText(refreshToken) && jwtUtils.isRefreshTokenValid(refreshToken)) {
                String userName = jwtUtils.getUserNameFromRefreshToken(refreshToken);

                String savedRefreshToken = refreshTokenCache.get(userName);

                if (StringUtils.hasText(savedRefreshToken)
                        && savedRefreshToken.equals(refreshToken)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                    return generateJwtResponse(userDetails);
                } else {
                    throw new RefreshTokenException(refreshToken, "Refresh token not found");
                }
            } else {
                throw new RefreshTokenException(refreshToken, "Token is not valid");
            }
        } catch (Exception ex) {
            String tokenExceptionMessage = jwtUtils.logTokenExceptionMessage(ex);

            throw new RefreshTokenException(refreshToken, tokenExceptionMessage);
        }
    }

    @Override
    public SimpleResponse logout(String userName) {
        refreshTokenCache.remove(userName);
        return new SimpleResponse("User " + userName + " has been logged out!");
    }

    @Override
    public boolean doPasswordsMatch(RegistrationDto registrationDto) {
        if (StringUtils.hasText(registrationDto.getPassword1())) {
            if (registrationDto.getPassword1().equals(registrationDto.getPassword2())) {
                log.info("password1 matches password2");
                return true;
            }
        }
        log.info("password1 doesn't match password2");

        return false;
    }

    @Override
    public RegistrationDto getRegDtoWithEncryptedPassword(RegistrationDto registrationDto) {
        registrationDto.setPassword1(passwordEncoder.encode(registrationDto.getPassword1()));
        registrationDto.setPassword2(null);

        return registrationDto;
    }

    private JwtResponse generateJwtResponse(UserDetails userDetails) {
        String accessToken = jwtUtils.generateAccessToken(userDetails);

        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        return JwtResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
