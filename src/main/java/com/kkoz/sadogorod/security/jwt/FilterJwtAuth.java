package com.kkoz.sadogorod.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkoz.sadogorod.security.dto.DtoAuthCredentials;
import com.kkoz.sadogorod.security.jwt.secret_key.ServiceJwtSecretKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Этот класс нигде не используется (за ненадобностью),
 * однако я оставил его исключительно в ознакомительных целях
 * для лучшего понимания работы фильтров и процесса аутентификации
 * в Spring Security. (+ прикручивание jwt)
 */
public class FilterJwtAuth extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authManager;
    private final ServiceJwtSecretKey serviceJwtSecretKey;

    public FilterJwtAuth(AuthenticationManager authManager,
                         ServiceJwtSecretKey serviceJwtSecretKey) {
        this.authManager = authManager;
        this.serviceJwtSecretKey = serviceJwtSecretKey;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        DtoAuthCredentials credentials = new ObjectMapper().readValue(
                request.getInputStream(), DtoAuthCredentials.class
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword()
        );

        Authentication authenticate = authManager.authenticate(authentication);

        return authenticate;

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {

        Date issuedAt = new Date();
        int jwtTimeToLive = 15; // 15 minutes is optimal choice

        byte[] secretKeyBytes     = Decoders.BASE64.decode(serviceJwtSecretKey.getActive().getSecretKey());
        SecretKey activeSecretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        String jwt = Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setSubject(authResult.getName())
                .claim("roles", authResult.getAuthorities())
                .setIssuedAt(issuedAt)
                .setExpiration(DateUtils.addMinutes(issuedAt, jwtTimeToLive))
                .signWith(activeSecretKey)
                .compact();

        response.addHeader("Authorization", "Bearer " + jwt);

    }
}
