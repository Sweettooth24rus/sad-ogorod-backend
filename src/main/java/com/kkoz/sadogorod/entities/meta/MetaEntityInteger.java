package com.kkoz.sadogorod.entities.meta;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * абстрактный класс для корректной работы методов hashCode()
 * и equals() вместо @data lombok
 */

@Getter
@Setter
@MappedSuperclass
public abstract class MetaEntityInteger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 13;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        MetaEntityInteger other = (MetaEntityInteger) obj;

        return Objects.equals(id, other.id);
    }
}
