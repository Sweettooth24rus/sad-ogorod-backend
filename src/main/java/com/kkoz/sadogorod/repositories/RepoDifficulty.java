package com.kkoz.sadogorod.repositories;

import com.kkoz.sadogorod.entities.recipe.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoDifficulty extends JpaRepository<Difficulty, Integer> {

}
