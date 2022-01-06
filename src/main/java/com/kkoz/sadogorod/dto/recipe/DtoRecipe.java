package com.kkoz.sadogorod.dto.recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kkoz.sadogorod.dto.file.DtoFileUpload;
import com.kkoz.sadogorod.entities.recipe.Recipe;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DtoRecipe {

    private String name;
    private String description;
    private DtoFileUpload photo;
    private Integer days;
    private String lightType;
    private Integer lightTime;
    private String groundType;
    private Integer minTemperature;
    private Integer maxTemperature;
    private String difficulty;
    private String comment;

    public DtoRecipe(Recipe entity) {
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.photo = new DtoFileUpload(entity.getPhoto());
        this.days = entity.getAdvice().getDays();
        this.lightType = entity.getAdvice().getLightType().getKey();
        this.lightTime = entity.getAdvice().getLightTime();
        this.groundType = entity.getAdvice().getGroundType().getKey();
        this.minTemperature = entity.getAdvice().getMinTemperature();
        this.maxTemperature = entity.getAdvice().getMaxTemperature();
        this.difficulty = entity.getAdvice().getDifficulty().getKey();
        this.comment = entity.getComment();
    }
}
