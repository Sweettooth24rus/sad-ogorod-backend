package com.kkoz.sadogorod.repositories;

import com.kkoz.sadogorod.entities.recipe.GroundType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoGroundType extends JpaRepository<GroundType, Integer> {

}
