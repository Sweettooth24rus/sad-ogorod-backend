package com.kkoz.sadogorod.security.jwt.refresh_token;

import com.kkoz.sadogorod.entities.meta.MetaEntityInteger;
import com.kkoz.sadogorod.entities.uzer.Uzer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "refresh_token",
        uniqueConstraints = @UniqueConstraint(columnNames = "uzer_id")
)
public class RefreshToken extends MetaEntityInteger {

    @OneToOne
    @JoinColumn(name = "uzer_id", referencedColumnName = "id")
    private Uzer uzer;

    @NotNull(message = "Refresh-token не может быть null")
    @Column(unique = true)
    private UUID refreshToken;

    @NotNull(message = "Время жизни refresh-token не должно быть null")
    private LocalDateTime expirationDateTime;

}
