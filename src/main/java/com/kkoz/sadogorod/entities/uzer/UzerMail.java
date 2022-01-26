package com.kkoz.sadogorod.entities.uzer;

import com.kkoz.sadogorod.entities.meta.MetaEntityInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class UzerMail extends MetaEntityInteger {

    private String username;
    private Boolean active;
}
