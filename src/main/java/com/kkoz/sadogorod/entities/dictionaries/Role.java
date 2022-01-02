package com.kkoz.sadogorod.entities.dictionaries;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Role extends EntityDictionary implements Serializable {

    public static final Role ADMIN    = new Role(1, "ADMIN", "Администратор");
    public static final Role MEGA     = new Role(2, "MEGA", "Мега");
    public static final Role HYPER    = new Role(3, "HYPER", "Гипер");
    public static final Role PLUS     = new Role(4, "PLUS", "Плюс");
    public static final Role ORDINARY = new Role(5, "ORDINARY", "Обычный");

    @Id
    @JsonIgnore
    private Integer id;

    @JsonProperty(value = "key")
    private String key;

    @JsonProperty(value = "value")
    private String value;

}
