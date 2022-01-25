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
public class GroundType extends EntityDictionary implements Serializable {

    public static final GroundType ALUMINA = new GroundType(1, "ALUMINA", "Глинозём");
    public static final GroundType LOAM = new GroundType(2, "LOAM", "Суглинок");
    public static final GroundType SANDSTONE = new GroundType(3, "SANDSTONE", "Песчаник");
    public static final GroundType SANDY_LOAM = new GroundType(4, "SANDY_LOAM", "Супесь");
    public static final GroundType PEAT_BOG = new GroundType(5, "PEAT_BOG", "Торфянник");
    public static final GroundType PODZOLIC = new GroundType(6, "PODZOLIC", "Подзолистая");
    public static final GroundType SOD_PODZOLIC = new GroundType(7, "SOD_PODZOLIC", "Дерново-подзолистая");
    public static final GroundType BLACK_SOIL = new GroundType(8, "BLACK_SOIL", "Чернозём");

    @Id
    @JsonIgnore
    private Integer id;

    private String key;

    private String value;
}
