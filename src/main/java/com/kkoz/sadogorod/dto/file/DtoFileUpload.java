package com.kkoz.sadogorod.dto.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kkoz.sadogorod.entities.file.FileUpload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DtoFileUpload implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String originalFileName;
    private String mimeType;
    private UUID uuid;
    private Long size;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime created;

    private String storePath;

    public DtoFileUpload(FileUpload entity) {
        this.id               = entity.getId();
        this.originalFileName = entity.getOriginalFileName();
        this.mimeType         = entity.getMimeType();
        this.uuid             = entity.getUuid();
        this.size             = entity.getSize();
        this.created          = entity.getCreated();
        this.storePath        = entity.getStorePath();
    }

}
