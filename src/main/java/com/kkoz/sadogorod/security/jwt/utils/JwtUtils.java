package com.kkoz.sadogorod.security.jwt.utils;


import com.kkoz.sadogorod.entities.uzer.Uzer;
import com.kkoz.sadogorod.security.jwt.refresh_token.RefreshToken;
import com.kkoz.sadogorod.security.jwt.refresh_token.ServiceRefreshToken;
import com.kkoz.sadogorod.security.jwt.secret_key.ServiceJwtSecretKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.time.to.live}")
    private int jwtTTL;

    @Value("${resource.jwt.time.to.live}")
    private int resourceJwtTTL;

    private final ServiceJwtSecretKey serviceJwtSecretKey;
    private final ServiceRefreshToken serviceRefreshToken;

    public String generateJwt(Uzer uzer) {
        Date issuedAt = new Date();

        byte[] secretKeyBytes     = Decoders.BASE64.decode(serviceJwtSecretKey.getActive().getSecretKey());
        SecretKey activeSecretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .claim("id", uzer.getId())
                .setSubject(uzer.getUsername())
                .claim("fio", uzer.getLastName() + " " + uzer.getFirstName() + " " + uzer.getPatronymicName())
                .claim("roles", uzer.getAuthorities())
                .setIssuedAt(issuedAt)
                .setExpiration(DateUtils.addMinutes(issuedAt, this.jwtTTL))
                .signWith(activeSecretKey)
                .compact();
    }

    public String generateRefreshToken(Uzer uzer) {
        RefreshToken rt = serviceRefreshToken.generate(uzer);

        return rt.getRefreshToken().toString();
    }

    public String generateResourceJwt(String requestedResource) {
        Date issuedAt = new Date();

        byte[] secretKeyBytes     = Decoders.BASE64.decode(serviceJwtSecretKey.getActive().getSecretKey());
        SecretKey activeSecretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        return Jwts.builder()
                .claim("res", requestedResource)
                .setIssuedAt(issuedAt)
                .setExpiration(DateUtils.addMinutes(issuedAt, this.resourceJwtTTL))
                .signWith(activeSecretKey)
                .compact();
    }

    public boolean verifyResourceJwt(String resourceJwt) {
        /*try {
            String requestUri;

            Optional<HttpServletRequest> optionalHttpServletRequest = HttpRequestUtil.getCurrentHttpRequest();
            if (optionalHttpServletRequest.isPresent()) {
                requestUri = optionalHttpServletRequest.get().getRequestURI();
            }
            else {
                throw new ResourceJwtValidationException();
            }

            byte[] secretKeyBytes     = Decoders.BASE64.decode(serviceJwtSecretKey.getActive().getSecretKey());
            SecretKey activeSecretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(activeSecretKey)
                    .build()
                    .parseClaimsJws(resourceJwt);

            Claims jwtPayload = claimsJws.getBody();

            String jwtRequestedResource = (String) jwtPayload.get("res");

            if(!jwtRequestedResource.equals(requestUri)) {
                throw new ResourceJwtValidationException();
            }

            return true;
        }
        catch (JwtExpirationException | JwtValidationException | MalformedJwtException | SignatureException | ExpiredJwtException e) {
            throw new ResourceJwtValidationException();
        }*/
        return true;
    }

}
