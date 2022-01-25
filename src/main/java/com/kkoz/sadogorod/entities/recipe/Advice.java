package com.kkoz.sadogorod.entities.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class Advice {

    private Integer days;
    @ManyToOne
    private LightType lightType;
    private Integer lightTime;
    @ManyToOne
    private GroundType groundType;
    private Integer minTemperature;
    private Integer maxTemperature;
    @ManyToOne
    private Difficulty difficulty;
}
