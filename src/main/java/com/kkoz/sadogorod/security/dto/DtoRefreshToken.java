package com.kkoz.sadogorod.security.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class DtoRefreshToken {

    @NotNull(message = "Refresh-token не должен быть пустым")
    private UUID refreshToken;

}
