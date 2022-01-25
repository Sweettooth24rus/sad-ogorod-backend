package com.kkoz.sadogorod.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kkoz.sadogorod.entities.Modules;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DtoModules {

    private Integer id;
    private String name;
    private Boolean activity;

    public DtoModules(Modules entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.activity = entity.getActivity();
    }
}
