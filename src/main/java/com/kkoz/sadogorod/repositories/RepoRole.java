package com.kkoz.sadogorod.repositories;

import com.kkoz.sadogorod.entities.dictionaries.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoRole extends JpaRepository<Role, Integer> {
}
