package com.kkoz.sadogorod.dto.recipe;

import com.kkoz.sadogorod.dto.file.DtoFileUpload;
import com.kkoz.sadogorod.entities.recipe.Recipe;
import lombok.Data;

@Data
public class DtoRecipePagination {

    private Integer id;
    private String name;
    private DtoFileUpload photo;
    private Integer days;
    private String difficulty;

    public DtoRecipePagination(Recipe entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.photo = new DtoFileUpload(entity.getFiles().stream().findFirst().get());
        this.days = entity.getDays();
        this.difficulty = entity.getDifficulty().getKey();
    }

}
