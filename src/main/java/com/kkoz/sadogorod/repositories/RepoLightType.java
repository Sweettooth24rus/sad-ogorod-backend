package com.kkoz.sadogorod.repositories;

import com.kkoz.sadogorod.entities.recipe.LightType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoLightType extends JpaRepository<LightType, Integer> {

}
