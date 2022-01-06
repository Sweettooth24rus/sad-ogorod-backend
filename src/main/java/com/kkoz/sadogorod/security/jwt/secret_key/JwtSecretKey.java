package com.kkoz.sadogorod.security.jwt.secret_key;

import com.kkoz.sadogorod.entities.meta.MetaEntityInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "jwt_secret_key")
public class JwtSecretKey extends MetaEntityInteger {

    @NotBlank
    @Column(unique = true)
    private String secretKey;

    @NotNull
    private Boolean isActive;

}
