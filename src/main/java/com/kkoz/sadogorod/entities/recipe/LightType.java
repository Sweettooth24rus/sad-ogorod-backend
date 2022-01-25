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
public class LightType extends EntityDictionary implements Serializable {

    public static final LightType DARK = new LightType(1, "DARK", "Тёмный");
    public static final LightType LOW = new LightType(2, "LOW", "Низкий (22 Вт/м2)");
    public static final LightType MEDIUM = new LightType(3, "MEDIUM", "Средний (45 Вт/м2)");
    public static final LightType HIGH = new LightType(4, "HIGH", "Высокий (75 Вт/м2)");
    public static final LightType VERY_HIGH = new LightType(5, "VERY_HIGH", "Очень высокий (135 Вт/м2)");

    @Id
    @JsonIgnore
    private Integer id;

    private String key;

    private String value;
}
