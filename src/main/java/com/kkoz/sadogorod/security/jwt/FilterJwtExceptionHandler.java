package com.kkoz.sadogorod.security.jwt;

import com.kkoz.sadogorod.security.jwt.exceptions.JwtExpirationException;
import com.kkoz.sadogorod.security.jwt.exceptions.JwtValidationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class FilterJwtExceptionHandler extends OncePerRequestFilter {

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {

        try {
            filterChain.doFilter(request, response);
        }
        catch (JwtExpirationException | ExpiredJwtException e) {
            JSONObject responseBody = new JSONObject();
            responseBody.put("error", e.getClass().getSimpleName());
            responseBody.put("message", "Срок действия jwt истёк");

            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(responseBody.toString());
        }
        catch (JwtValidationException | MalformedJwtException | SignatureException e) {
            JSONObject responseBody = new JSONObject();
            responseBody.put("error", e.getClass().getSimpleName());
            responseBody.put("message", "Предъявленный jwt не прошел проверку");

            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.I_AM_A_TEAPOT.value());
            response.getWriter().write(responseBody.toString());
        }

    }

}
