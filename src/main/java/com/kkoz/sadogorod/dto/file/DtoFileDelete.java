package com.kkoz.sadogorod.dto.file;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class DtoFileDelete {

    private Integer entityId;

    @NotNull(message = "UUID файла не должно быть null")
    private UUID uuid;

}
