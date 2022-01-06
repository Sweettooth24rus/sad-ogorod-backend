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

    @Id
    @JsonIgnore
    private Integer id;

    private String key;

    private String value;
}
