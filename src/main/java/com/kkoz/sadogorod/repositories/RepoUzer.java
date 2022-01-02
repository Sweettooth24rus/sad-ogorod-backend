package com.kkoz.sadogorod.repositories;

import com.kkoz.sadogorod.entities.uzer.Uzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoUzer extends JpaRepository<Uzer, Integer>, JpaSpecificationExecutor<Uzer> {
    Optional<Uzer> getUzerById(Integer id);
    Optional<Uzer> getByUsername(String username);

    Optional<Uzer> getByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);
    boolean existsByUsernameAndIsActiveTrue(String username);
    boolean existsByUsernameAndIsActiveFalse(String username);
}
