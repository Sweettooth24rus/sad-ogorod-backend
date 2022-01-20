package com.kkoz.sadogorod.entities.uzer;

import com.kkoz.sadogorod.entities.meta.MetaEntityInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Uzer extends MetaEntityInteger implements UserDetails {

    private String username;

    private String password;

    private String lastName;

    private String firstName;

    private String patronymicName;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UzerRole role;

    private Boolean isActive = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.getRole());
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.getIsActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.getIsActive();
    }

    @Override
    public boolean isEnabled() {
        return this.getIsActive();
    }
}
