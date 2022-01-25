package com.kkoz.sadogorod.repositories;

import com.kkoz.sadogorod.entities.uzer.UzerMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoUzerMail extends JpaRepository<UzerMail, Integer> {

    Optional<UzerMail> getByUsername(String username);

    @Query(value = "SELECT nextval('uzer_mail_seq')", nativeQuery = true)
    Integer createApplicationNumber();
}
