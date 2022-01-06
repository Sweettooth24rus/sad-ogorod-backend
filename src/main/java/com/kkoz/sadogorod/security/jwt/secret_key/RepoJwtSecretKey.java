package com.kkoz.sadogorod.security.jwt.secret_key;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoJwtSecretKey extends JpaRepository<JwtSecretKey, Integer> {

    Optional<JwtSecretKey> getByIsActiveTrue();
    List<JwtSecretKey> getAllByIsActiveTrue();

}
