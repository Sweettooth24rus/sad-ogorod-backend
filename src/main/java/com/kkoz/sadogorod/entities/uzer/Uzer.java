package com.kkoz.sadogorod.entities.uzer;

import com.kkoz.sadogorod.entities.meta.MetaEntityInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Uzer extends MetaEntityInteger {

    private String username;

    private String password;

    private String lastName;

    private String firstName;

    private String patronymicName;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UzerRole role;

    private Boolean isActive = true;
}
