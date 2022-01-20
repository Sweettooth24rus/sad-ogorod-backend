package com.kkoz.sadogorod.dto.uzer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kkoz.sadogorod.entities.uzer.Uzer;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DtoUzerUpdate {

    private Integer id;

    @NotBlank(message = "Логин не должен быть пустым")
    private String username;

    private String password;

    @NotBlank(message = "Фамилия не должна быть пустой")
    private String lastName;

    @NotBlank(message = "Имя не должно быть пустым")
    private String firstName;

    private String patronymicName;

    @NotBlank(message = "Электронный адрес должен быть валидным")
    @Email(message = "Электронный адрес должен быть валидным")
    private String email;

    private String phone;

    @NotEmpty(message = "Список ролей не должен быть пустым")
    private String role;

    private Boolean isActive;

    public DtoUzerUpdate(Uzer entity) {
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

    public DtoUzerUpdate(DtoUzer dtoUzer) {
        this.id = dtoUzer.getId();
        this.username = dtoUzer.getUsername();
        this.password = "";
        this.lastName = dtoUzer.getLastName();
        this.firstName = dtoUzer.getFirstName();
        this.patronymicName = dtoUzer.getPatronymicName();
        this.email = dtoUzer.getEmail();
        this.phone = dtoUzer.getPhone();
        this.role = dtoUzer.getRole();
        this.isActive = dtoUzer.getIsActive();
    }
}
