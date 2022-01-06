package com.kkoz.sadogorod.entities.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LightType {
    DARK("DARK", "Тёмный"),
    LOW("LOW", "Низкий (22 Вт/м2)"),
    MEDIUM("MEDIUM", "Средний (45 Вт/м2)"),
    HIGH("HIGH", "Высокий (75 Вт/м2)"),
    VERY_HIGH("VERY_HIGH", "Очень высокий (135 Вт/м2)");

    private String key;

    private String value;
}
