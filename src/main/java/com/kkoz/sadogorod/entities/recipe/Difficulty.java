package com.kkoz.sadogorod.entities.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kkoz.sadogorod.entities.dictionaries.EntityDictionary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Difficulty extends EntityDictionary implements Serializable {
    public static final Difficulty VERY_EASY = new Difficulty(1, "VERY_EASY", "Очень просто");
    public static final Difficulty EASY = new Difficulty(2, "EASY", "Просто");
    public static final Difficulty NORMAL = new Difficulty(3, "NORMAL", "Нормально");
    public static final Difficulty HARD = new Difficulty(4, "HARD", "Сложно");
    public static final Difficulty VERY_HARD = new Difficulty(5, "VERY_HARD", "Очень сложно");

    @Id
    @JsonIgnore
    private Integer id;

    private String key;

    private String value;
}
