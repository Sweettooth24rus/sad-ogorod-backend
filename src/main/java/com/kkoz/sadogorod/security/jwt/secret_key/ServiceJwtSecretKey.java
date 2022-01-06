package com.kkoz.sadogorod.security.jwt.secret_key;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ServiceJwtSecretKey {

    private final RepoJwtSecretKey repoJwtSecretKey;

    public ServiceJwtSecretKey(RepoJwtSecretKey repoJwtSecretKey) {
        this.repoJwtSecretKey = repoJwtSecretKey;
    }

    public DtoJwtSecretKey getActive() {
        //log.debug("[jwt] Getting active jwt secret key...");

        Optional<JwtSecretKey> secretKey = repoJwtSecretKey.getByIsActiveTrue();

        DtoJwtSecretKey dtoJwtSecretKey = new DtoJwtSecretKey(secretKey.orElseThrow(
                () -> new JwtSecretKeyException("There is no active jwt secret key")
        ));

        //log.debug("[jwt] Got active jwt secret key.");

        return dtoJwtSecretKey;
    }

    public DtoJwtSecretKey generateNewActive() {
        log.debug("[jwt] Generating new active jwt secret key...");

        this.invalidateOldActive();

        JwtSecretKey entity = new JwtSecretKey();
        entity.setSecretKey(this.generateNewJwtSecretKey());
        entity.setIsActive(true);

        JwtSecretKey newActiveJwtSecretKey = repoJwtSecretKey.save(entity);

        log.debug("[jwt] New active jwt secret key generated.");

        return new DtoJwtSecretKey(newActiveJwtSecretKey);
    }

    private void invalidateOldActive() {
        log.debug("[jwt] Invalidating old active jwt secret key...");

        List<JwtSecretKey> jwtSecretKeyList = repoJwtSecretKey.getAllByIsActiveTrue();

        if (jwtSecretKeyList != null && !jwtSecretKeyList.isEmpty()) {
            for (JwtSecretKey jsk : jwtSecretKeyList) {
                jsk.setIsActive(false);
            }

            repoJwtSecretKey.saveAll(jwtSecretKeyList);
        }

        log.debug("[jwt] Old active jwt secret key invalidated.");
    }

    private String generateNewJwtSecretKey() {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        return Encoders.BASE64.encode(secretKey.getEncoded());
    }

}
