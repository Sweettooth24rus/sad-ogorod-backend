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

    private Integer id;
    private String name;
    private String description;
    private DtoFileUpload photo;
    private Integer days;
    private Integer lightType;
    private Integer lightTime;
    private Integer groundType;
    private Integer minTemperature;
    private Integer maxTemperature;
    private Integer difficulty;
    private String comment;

    public DtoRecipe(Recipe entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.photo = new DtoFileUpload(entity.getPhoto());
        this.days = entity.getAdvice().getDays();
        this.lightType = entity.getAdvice().getLightType().getId();
        this.lightTime = entity.getAdvice().getLightTime();
        this.groundType = entity.getAdvice().getGroundType().getId();
        this.minTemperature = entity.getAdvice().getMinTemperature();
        this.maxTemperature = entity.getAdvice().getMaxTemperature();
        this.difficulty = entity.getAdvice().getDifficulty().getId();
        this.comment = entity.getComment();
    }
}
