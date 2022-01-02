package com.kkoz.sadogorod.dto.uzer;

import com.kkoz.sadogorod.entities.uzer.Uzer;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class DtoUzerPagination {

    private String username;
    private String email;
    private String role;
    private String banUntil;

    public DtoUzerPagination(Uzer entity) {
        this.username = entity.getUsername();
        this.email = entity.getEmail();
        this.role = entity.getRole().getKey();
        this.banUntil = entity.getIsActive()
                ? null
                : entity.getBanUntil().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

}
