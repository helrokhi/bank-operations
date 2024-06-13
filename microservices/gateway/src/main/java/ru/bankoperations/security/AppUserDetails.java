package ru.bankoperations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.bankoperations.dto.*;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {
    private final UserAuthDto userAuthDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority grantedAuthority =
                new SimpleGrantedAuthority("ROLE_USER");
        return Collections.singleton(grantedAuthority);
    }

    @Override
    public String getPassword() {
        return userAuthDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userAuthDto.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
