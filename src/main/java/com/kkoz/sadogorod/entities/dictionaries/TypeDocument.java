package com.kkoz.sadogorod.entities.dictionaries;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class TypeDocument extends EntityDictionary implements Serializable {

    public static final TypeDocument RECIPE_PHOTO = new TypeDocument(1, "RECIPE_PHOTO", "Фотография в рецепте");
    public static final TypeDocument PICKLE_PHOTO = new TypeDocument(2, "PICKLE_PHOTO", "Фотография в соленьях");
    public static final TypeDocument TEA_PHOTO = new TypeDocument(3, "TEA_PHOTO", "Фотография в чаях");
    public static final TypeDocument GARDEN_PHOTO = new TypeDocument(4, "GARDEN_PHOTO", "Фотография в огородах");
    public static final TypeDocument WEED_PHOTO = new TypeDocument(5, "WEED_PHOTO", "Фотография в сорняках");
    public static final TypeDocument GREENHOUSE_PHOTO = new TypeDocument(6, "GREENHOUSE_PHOTO", "Фотография в теплицах");

    @Id
    @JsonIgnore
    private Integer id;

    private String key;

    private String value;
}
