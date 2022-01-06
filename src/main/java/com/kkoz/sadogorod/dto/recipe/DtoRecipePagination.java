package com.kkoz.sadogorod.dto.recipe;

import com.kkoz.sadogorod.dto.file.DtoFileUpload;
import com.kkoz.sadogorod.entities.recipe.Recipe;
import lombok.Data;

@Data
public class DtoRecipePagination {

    private String name;
    private DtoFileUpload photo;
    private Integer days;
    private String difficulty;

    public DtoRecipePagination(Recipe entity) {
        this.name = entity.getName();
        this.photo = new DtoFileUpload(entity.getPhoto());
        this.days = entity.getAdvice().getDays();
        this.difficulty = entity.getAdvice().getDifficulty().getKey();
    }

}
