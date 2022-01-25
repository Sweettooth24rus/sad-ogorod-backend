package com.kkoz.sadogorod.repositories;

import com.kkoz.sadogorod.entities.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoRecipe extends JpaRepository<Recipe, Integer>, JpaSpecificationExecutor<RepoRecipe> {

}
