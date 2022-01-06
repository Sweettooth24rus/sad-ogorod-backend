package com.kkoz.sadogorod.entities.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GroundType {
    ALUMINA("ALUMINA", "Глинозём"),
    LOAM("LOAM", "Суглинок"),
    SANDSTONE("SANDSTONE", "Песчаник"),
    SANDY_LOAM("SANDY_LOAM", "Супесь"),
    PEAT_BOG("PEAT_BOG", "Торфянник"),
    PODZOLIC("PODZOLIC", "Подзолистая"),
    SOD_PODZOLIC("SOD_PODZOLIC", "Дерново-подзолистая"),
    BLACK_SOIL("BLACK_SOIL", "Чернозём");

    private String key;

    private String value;
}
