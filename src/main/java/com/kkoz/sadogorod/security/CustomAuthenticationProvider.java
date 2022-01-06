package com.kkoz.sadogorod.security;

import com.kkoz.sadogorod.entities.uzer.Uzer;
import com.kkoz.sadogorod.security.dto.DtoAuthCredentials;
import com.kkoz.sadogorod.services.ServiceUzer;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final ServiceUzer serviceUzer;

    public CustomAuthenticationProvider(ServiceUzer serviceUzer) {
        this.serviceUzer = serviceUzer;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        Uzer uzer;

        if (serviceUzer.existsByUsername(username)) {
            uzer = serviceUzer.getByCredentials(new DtoAuthCredentials(username, password));
            if (!uzer.getIsActive()) {
                throw new LockedException("Неверно указан логин или пароль");
            }
        }
        else {
            throw new InternalAuthenticationServiceException("Неверно указан логин или пароль");
        }

        return new UsernamePasswordAuthenticationToken(
                uzer,
                null,
                uzer.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
