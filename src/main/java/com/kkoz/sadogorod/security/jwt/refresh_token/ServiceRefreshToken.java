package com.kkoz.sadogorod.security.jwt.refresh_token;

import com.kkoz.sadogorod.entities.uzer.Uzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ServiceRefreshToken {

    @Value("${refresh.token.time.to.live}")
    private long refreshTokenTTL;

    private final RepoRefreshToken repoRefreshToken;

    public ServiceRefreshToken(RepoRefreshToken repoRefreshToken) {
        this.repoRefreshToken = repoRefreshToken;
    }

    public RefreshToken generate(Uzer uzer) {
        Optional<RefreshToken> ort = repoRefreshToken.getByUzer(uzer);

        RefreshToken rt = ort.orElse(new RefreshToken());

        rt.setUzer(uzer);
        rt.setRefreshToken(UUID.randomUUID());
        rt.setExpirationDateTime(LocalDateTime.now().plusMinutes(this.refreshTokenTTL));

        return repoRefreshToken.save(rt);
    }

    public boolean isExpired(UUID refreshToken) {
        RefreshToken rt = repoRefreshToken.getByRefreshToken(refreshToken);

        LocalDateTime refreshTokenExpirationDateTime = rt.getExpirationDateTime();

        if (refreshTokenExpirationDateTime.isBefore(LocalDateTime.now())) {
            repoRefreshToken.delete(rt);
            return true;
        }

        return false;
    }

    public boolean existsByRefreshToken(UUID refreshToken) {
        return repoRefreshToken.existsByRefreshToken(refreshToken);
    }

    public Uzer getUzerByRefreshToken(UUID refreshToken) {
        return repoRefreshToken.getByRefreshToken(refreshToken).getUzer();
    }

    public boolean existsByUzer(Uzer uzer) {
        return repoRefreshToken.existsByUzer(uzer);
    }

    public void deleteByUzer(Uzer uzer) {
        repoRefreshToken.deleteByUzer(uzer);
    }

}
