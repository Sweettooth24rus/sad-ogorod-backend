package com.kkoz.sadogorod.dto.uzer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kkoz.sadogorod.entities.uzer.Uzer;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DtoUzer {

    private Integer id;
    private String username;
    private String password;
    private String lastName;
    private String firstName;
    private String patronymicName;
    private String email;
    private String phone;
    private String role;
    private Boolean isActive;

    public DtoUzer(Uzer entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.password = "";
        this.lastName = entity.getLastName();
        this.firstName = entity.getFirstName();
        this.patronymicName = entity.getPatronymicName();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.role = entity.getRole().getKey();
        this.isActive = entity.getIsActive();
    }
}
