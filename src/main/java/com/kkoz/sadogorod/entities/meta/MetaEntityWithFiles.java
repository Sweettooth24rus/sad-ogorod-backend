package com.kkoz.sadogorod.entities.meta;

import com.kkoz.sadogorod.entities.file.FileUpload;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public abstract class MetaEntityWithFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Файлы, прикреплённые к доменной сущности
     */
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<FileUpload> files = new ArrayList<>();

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 13;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        MetaEntityWithFiles other = (MetaEntityWithFiles) obj;

        return Objects.equals(id, other.id);
    }

}
