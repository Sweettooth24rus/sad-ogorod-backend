package com.kkoz.sadogorod.entities.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kkoz.sadogorod.entities.dictionaries.TypeDocument;
import com.kkoz.sadogorod.entities.meta.MetaEntityInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class FileUpload extends MetaEntityInteger {

    private String originalFileName;
    private String mimeType;
    private UUID uuid;
    private Long size;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime created;

    private String storePath;

    @JoinColumn(name = "key_type_document", foreignKey = @ForeignKey(name = "FK_FILE_UPLOAD__TYPE_DOCUMENT"))
    @ManyToOne
    private TypeDocument typeDocument;

}
