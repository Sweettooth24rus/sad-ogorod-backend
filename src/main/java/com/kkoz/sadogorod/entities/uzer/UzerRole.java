package com.kkoz.sadogorod.entities.uzer;


import com.kkoz.sadogorod.controls.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public enum UzerRole implements GrantedAuthority {

    ADMIN    ("ADMIN", "Администратор"),
    MEGA     ("MEGA", "Мега"),
    HYPER    ("HYPER", "Гипер"),
    PLUS     ("PLUS", "Плюс"),
    ORDINARY ("ORDINARY", "Обычный");

    private final String key;
    private final String value;

    @Override
    public String getAuthority() {
        return this.toAuthority().getAuthority();
    }

    public GrantedAuthority toAuthority() {
        return () -> String.format("ROLE_%s", this.name());
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static UzerRole getRoleFromValue(String value) {
        for (UzerRole role : UzerRole.values()) {
            if (role.getValue().equals(value)) {
                return role;
            }
        }
        throw new NotFoundException(UzerRole.class.getSimpleName(), "value", value);
    }

    public static UzerRole getRoleFromKey(String key) {
        for (UzerRole role : UzerRole.values()) {
            if (role.getKey().equals(key)) {
                return role;
            }
        }
        throw new NotFoundException(UzerRole.class.getSimpleName(), "key", key);
    }

}
