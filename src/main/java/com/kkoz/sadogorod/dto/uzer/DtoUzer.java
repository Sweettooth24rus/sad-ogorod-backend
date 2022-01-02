package com.kkoz.sadogorod.dto.uzer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kkoz.sadogorod.entities.uzer.Uzer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DtoUzer {

    private String username;
    private String password;
    private String lastName;
    private String firstName;
    private String patronymicName;
    private String email;
    private String phone;
    private String role;
    private String banUntil;

    public DtoUzer(Uzer entity) {
        this.username = entity.getUsername();
        this.password = "";
        this.lastName = entity.getLastName();
        this.firstName = entity.getFirstName();
        this.patronymicName = entity.getPatronymicName();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.role = entity.getRole().getKey();
        this.banUntil = entity.getIsActive()
                ? null
                : entity.getBanUntil().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
