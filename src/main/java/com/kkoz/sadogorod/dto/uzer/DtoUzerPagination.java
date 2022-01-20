package com.kkoz.sadogorod.dto.uzer;

import com.kkoz.sadogorod.entities.uzer.Uzer;
import lombok.Data;

@Data
public class DtoUzerPagination {

    private String username;
    private String email;
    private String role;
    private Boolean isActive;

    public DtoUzerPagination(Uzer entity) {
        this.username = entity.getUsername();
        this.email = entity.getEmail();
        this.role = entity.getRole().getKey();
        this.isActive = entity.getIsActive();
    }

}
