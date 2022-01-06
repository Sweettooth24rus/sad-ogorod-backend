package com.kkoz.sadogorod.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoLogUzer {
    private Integer id;
    private String  username;
    private String  fio;
    private Set<String> roles;

    public String printLog() {
        return "User(id["+ this.id +"], " +
               "username["+ this.username +"], " +
               "fio["+ this.fio +"], " +
               "roles"+ this.roles +")";
    }
}
