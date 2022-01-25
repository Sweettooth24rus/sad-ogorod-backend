package com.kkoz.sadogorod.filters;

import com.kkoz.sadogorod.entities.recipe.Advice_;
import com.kkoz.sadogorod.entities.recipe.Difficulty;
import com.kkoz.sadogorod.entities.recipe.Recipe;
import com.kkoz.sadogorod.entities.recipe.Recipe_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SpecRecipe {

    public Specification<Recipe> getNameFilter(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> (
                criteriaBuilder.like(criteriaBuilder.lower(root.get(Recipe_.NAME)),
                        "%" + name.toLowerCase() + "%"
                )
        );
    }

    public Specification<Recipe> getDifficultyFilter(Difficulty difficulty) {
        return (root, criteriaQuery, criteriaBuilder) -> (
                criteriaBuilder.equal(root.get(Recipe_.ADVICE).get(Advice_.DIFFICULTY), difficulty
                )
        );
    }
}
