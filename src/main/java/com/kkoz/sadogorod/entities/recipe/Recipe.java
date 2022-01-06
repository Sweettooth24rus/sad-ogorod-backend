package com.kkoz.sadogorod.entities.recipe;

import com.kkoz.sadogorod.entities.file.FileUpload;
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
public class Recipe extends MetaEntityInteger {

    private String name;
    private String description;
    private FileUpload photo;
}
