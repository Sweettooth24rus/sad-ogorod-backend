package com.kkoz.sadogorod.entities.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class Advice {

    private Integer days;
    @Enumerated(EnumType.STRING)
    private LightType lightType;
    private Integer lightTime;
    @Enumerated(EnumType.STRING)
    private GroundType groundType;
    private Integer minTemperature;
    private Integer maxTemperature;
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
}
