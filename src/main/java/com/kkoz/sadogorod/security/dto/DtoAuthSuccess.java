package com.kkoz.sadogorod.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class DtoAuthSuccess {

    // JSON Web Token
    @NotBlank
    private String jwt;

    // JWT Refresh-token
    @NotBlank
    private String refreshToken;

}
