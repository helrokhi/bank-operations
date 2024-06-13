package ru.bankoperations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bankoperations.dto.UserAuthDto;
import ru.bankoperations.service.UserService;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthDto authData =
                userService
                        .findUserByLogin(username)
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                "User not found. Username is: " + username));

        return new AppUserDetails(authData);
    }
}
