package com.kkoz.sadogorod.entities.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Difficulty {
    VERY_EASY("VERY_EASY", "Очень просто"),
    EASY("EASY", "Просто"),
    NORMAL("NORMAL", "Нормально"),
    HARD("HARD", "Сложно"),
    VERY_HARD("VERY_HARD", "Очень сложно");

    private String key;

    private String value;
}
