package com.kkoz.sadogorod.security.jwt.refresh_token;

import com.kkoz.sadogorod.entities.uzer.Uzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RepoRefreshToken extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> getByUzer(Uzer uzer);

    boolean existsByRefreshToken(UUID refreshToken);

    RefreshToken getByRefreshToken(UUID refreshToken);

    boolean existsByUzer(Uzer uzer);

    void deleteByUzer(Uzer uzer);

}
