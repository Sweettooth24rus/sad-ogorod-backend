package com.kkoz.sadogorod.entities.recipe;

import com.kkoz.sadogorod.entities.file.FileUpload;
import com.kkoz.sadogorod.entities.meta.MetaEntityInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Recipe extends MetaEntityInteger {

    private String name;
    private String description;
    @ManyToOne
    private FileUpload photo;
    @Embedded
    private Advice advice;
    private String comment;
}
