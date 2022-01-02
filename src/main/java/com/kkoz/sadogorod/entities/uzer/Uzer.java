package com.kkoz.sadogorod.entities.uzer;

import com.kkoz.sadogorod.entities.meta.MetaEntityInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Uzer extends MetaEntityInteger implements UserDetails {

    @NotBlank(message = "Логин не должен быть пустым")
    private String username;

    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;

    @NotBlank(message = "Фамилия не должна быть пустой")
    private String lastName;

    @NotBlank(message = "Имя не должно быть пустым")
    private String firstName;

    private String patronymicName;

    @NotBlank(message = "Электронный адрес должен быть валидным")
    @Email(message = "Электронный адрес должен быть валидным")
    private String email;

    @Transient
    public String getFIO() {
        String family = lastName != null && !lastName.equals("") ? lastName : "";
        String name = firstName != null && !firstName.equals("") ? firstName : "";
        String patronymic = patronymicName != null && !patronymicName.equals("") ? patronymicName : "";
        return family + " " + name + " " + patronymic;
    }

    /**
     * Отдаёт сокращённую форму полного ФИО.
     * Пример: <b>"Иванов И. И."</b> или <b>"Петров П."</b>, если Отчества нет
     */
    @Transient
    public String getFIO2() {
        String lastName = (this.lastName != null && !this.lastName.isBlank()) ? this.lastName : "";
        String firstName = (this.firstName != null && !this.firstName.isBlank()) ? this.firstName.charAt(0) + "." : "";
        String patronymicName = (this.patronymicName != null && !this.patronymicName.isBlank()) ? this.patronymicName.charAt(0) + "." : "";

        return lastName + " " + firstName + " " + patronymicName;
    }

    @NotNull
    @ElementCollection(targetClass = UzerRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "uzer_role",
            joinColumns = @JoinColumn(name = "key_uzer"),
            foreignKey = @ForeignKey(name = "FK_UZER_ROLES"))
    @Enumerated(EnumType.STRING)
    private Set<UzerRole> roles = new HashSet<>();

    @NotNull(message = "Флаг активности пользователя не может быть null")
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.getIsActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.getIsActive();
    }

    @Override
    public boolean isEnabled() {
        return this.getIsActive();
    }
}
