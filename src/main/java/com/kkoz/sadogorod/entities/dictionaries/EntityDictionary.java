package com.kkoz.sadogorod.entities.dictionaries;

import java.util.Objects;

/**
 * абстрактный класс для словарных сущностей
 */

public abstract class EntityDictionary {
    public abstract Integer getId();
    public abstract String getKey();
    public abstract String getValue();

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 13;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        EntityDictionary other = (EntityDictionary) obj;

        return Objects.equals(getId(), other.getId());
    }
}
