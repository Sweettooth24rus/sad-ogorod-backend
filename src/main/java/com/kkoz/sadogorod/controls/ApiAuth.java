package com.kkoz.sadogorod.controls;

import com.kkoz.sadogorod.entities.uzer.Uzer;
import com.kkoz.sadogorod.security.CustomAuthenticationProvider;
import com.kkoz.sadogorod.security.dto.DtoAuthCredentials;
import com.kkoz.sadogorod.security.dto.DtoAuthSuccess;
import com.kkoz.sadogorod.security.dto.DtoRefreshToken;
import com.kkoz.sadogorod.security.jwt.exceptions.RefreshTokenExpiredException;
import com.kkoz.sadogorod.security.jwt.exceptions.RefreshTokenNotFoundException;
import com.kkoz.sadogorod.security.jwt.refresh_token.ServiceRefreshToken;
import com.kkoz.sadogorod.security.jwt.utils.JwtUtils;
import com.kkoz.sadogorod.security.meta_annotation.HasRoleAny;
import com.kkoz.sadogorod.services.ServiceUzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class ApiAuth {

    private final JwtUtils jwtUtils;
    private final ServiceUzer serviceUzer;
    private final ServiceRefreshToken serviceRefreshToken;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    public ApiAuth(JwtUtils jwtUtils,
                   ServiceUzer serviceUzer,
                   ServiceRefreshToken serviceRefreshToken,
                   CustomAuthenticationProvider customAuthenticationProvider) {
        this.jwtUtils = jwtUtils;
        this.serviceUzer = serviceUzer;
        this.serviceRefreshToken = serviceRefreshToken;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<DtoAuthSuccess> login(@Valid @RequestBody DtoAuthCredentials credentials) throws LoginException {
        log.info("-> POST: Authentication: uzername[{}]", credentials.getUsername());

        Authentication authentication;

        try {
            authentication = customAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword()
                    )
            );
        } catch (InternalAuthenticationServiceException e) {
            log.debug("<- POST: Authentication failed. There is no such uzer with uzername[{}]", credentials.getUsername());
            throw new LoginException("Неверно указан логин или пароль");
        } catch (BadCredentialsException e) {
            log.debug("<- POST: Authentication failed. Bad credentials for uzername[{}]", credentials.getUsername());
            throw new LoginException("Неверно указан логин или пароль");
        } catch (LockedException e) {
            log.debug("<- POST: Authentication failed. Inactive uzername[{}]", credentials.getUsername());
            throw new LoginException("Неверно указан логин или пароль");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Uzer uzer = (Uzer) authentication.getPrincipal();

        String jwt          = jwtUtils.generateJwt(uzer);
        String refreshToken = jwtUtils.generateRefreshToken(uzer);

        log.info("<- POST: Authentication success: id[{}], uzername[{}], role[{}]",
                uzer.getId(), uzer.getUsername(), uzer.getRole());

        return ResponseEntity.ok(new DtoAuthSuccess(jwt, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<DtoAuthSuccess> refresh(@Valid @RequestBody DtoRefreshToken refreshToken) {

        log.info("-> POST: JWT renewal attempt with refresh-token: {}", refreshToken.getRefreshToken());

        if (!serviceRefreshToken.existsByRefreshToken(refreshToken.getRefreshToken())) {
            log.info("<- POST: Refresh-token not found. Login again, please");
            throw new RefreshTokenNotFoundException("Refresh-token не найден. Пожалуйста, авторизуйтесь снова.");
        }

        if (serviceRefreshToken.isExpired(refreshToken.getRefreshToken())) {
            log.info("<- POST: Refresh-token is expired. Login again, please");
            throw new RefreshTokenExpiredException("Время жизни refresh-token истекло. Пожалуйста, авторизуйтесь снова.");
        }

        Uzer uzer = serviceRefreshToken.getUzerByRefreshToken(refreshToken.getRefreshToken());

        String jwt = jwtUtils.generateJwt(uzer);
        String rt  = jwtUtils.generateRefreshToken(uzer);

        log.info("<- POST: JWT renewal success: id[{}], uzername[{}], role[{}]",
                uzer.getId(), uzer.getUsername(), uzer.getRole());

        return ResponseEntity.ok(new DtoAuthSuccess(jwt, rt));

    }

    @HasRoleAny
    @Transactional
    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        log.info("-> GET: Logout for uzername[{}]", username);

        Uzer uzer = serviceUzer.getByUsername(username);

        if (serviceRefreshToken.existsByUzer(uzer)) {
            serviceRefreshToken.deleteByUzer(uzer);
        }

        Map<String, String> response = new HashMap<>();
        response.put("response", "Выход успешно осуществлён.");

        log.info("<- GET: Logout success: id[{}], uzername[{}], role[{}]",
                uzer.getId(), uzer.getUsername(), uzer.getRole());

        return ResponseEntity.ok(response);
    }

}
