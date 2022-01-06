package com.kkoz.sadogorod.security.jwt.secret_key;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoJwtSecretKey {

    private Integer id;
    private String secretKey;
    private Boolean isActive;

    public DtoJwtSecretKey(JwtSecretKey entity) {
        this.id        = entity.getId();
        this.secretKey = entity.getSecretKey();
        this.isActive  = entity.getIsActive();
    }

}
