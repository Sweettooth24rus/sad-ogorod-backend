package com.kkoz.sadogorod.repositories;

import com.kkoz.sadogorod.entities.Modules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoModules extends JpaRepository<Modules, Integer> {
}
