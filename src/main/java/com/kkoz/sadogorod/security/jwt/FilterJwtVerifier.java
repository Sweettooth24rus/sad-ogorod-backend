package com.kkoz.sadogorod.security.jwt;

import com.kkoz.sadogorod.security.dto.DtoLogUzer;
import com.kkoz.sadogorod.security.jwt.exceptions.JwtExpirationException;
import com.kkoz.sadogorod.security.jwt.exceptions.JwtValidationException;
import com.kkoz.sadogorod.security.jwt.secret_key.ServiceJwtSecretKey;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class FilterJwtVerifier extends OncePerRequestFilter {

    private final ServiceJwtSecretKey serviceJwtSecretKey;

    public FilterJwtVerifier(ServiceJwtSecretKey serviceJwtSecretKey) {
        this.serviceJwtSecretKey = serviceJwtSecretKey;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {

        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.replace("Bearer ", "");

        try {
            byte[] secretKeyBytes = Decoders.BASE64.decode(serviceJwtSecretKey.getActive().getSecretKey());
            SecretKey activeSecretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

            /*
                Код ниже делает попытку извлечь данные из jwt в соответствии
                с активным секретным клюем. Исключения, сгенерированные при
                извлечении обрабатываются в security/jwt/FilterJwtExceptionHandler.java
             */

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(activeSecretKey)
                    .build()
                    .parseClaimsJws(jwt);

            /*
                Код ниже извлекает из jwt субъект токена (uzername пользователя)
                и его набор ролей. Эти данные можно использовать, чтобы установить
                новое значение аутентификации пользователя с валидным jwt
                через SecurityContextHolder.
             */

            Claims jwtPayload = claimsJws.getBody();

            List<String> roleList = (List<String>) jwtPayload.get("roles");

            Set<SimpleGrantedAuthority> roles = roleList.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            Integer id          = (Integer) jwtPayload.get("id");
            String uzername     = jwtPayload.getSubject();
            String fio          = (String) jwtPayload.get("fio");
            Set<String> roleSet = new HashSet<>(roleList);

            DtoLogUzer logUzer  = new DtoLogUzer(id, uzername, fio, roleSet);

            request.setCharacterEncoding("UTF-8");
            request.setAttribute("log_user", logUzer);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    uzername,
                    null,
                    roles
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            log.warn("[jwt] Token [{}] is expired: {}", jwt, e.getMessage());
            throw new JwtExpirationException("Срок действия jwt истёк");

        } catch (SignatureException e) {
            log.error("[jwt] This token cannot be trusted: {}", jwt);
            throw new JwtValidationException("Предъявленный jwt не прошел проверку");
        }

        filterChain.doFilter(request, response);

    }

}
