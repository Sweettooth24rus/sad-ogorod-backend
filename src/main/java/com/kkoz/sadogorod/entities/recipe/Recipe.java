package com.kkoz.sadogorod.entities.recipe;

import com.kkoz.sadogorod.entities.meta.MetaEntityWithFiles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Recipe extends MetaEntityWithFiles {

    private String name;
    @Column(columnDefinition="TEXT")
    private String description;

    private Integer days;
    @ManyToOne(fetch = FetchType.EAGER)
    private LightType lightType;
    private Integer lightTime;
    @ManyToOne(fetch = FetchType.EAGER)
    private GroundType groundType;
    private Integer minTemperature;
    private Integer maxTemperature;
    @ManyToOne(fetch = FetchType.EAGER)
    private Difficulty difficulty;

    private String comment;
}
