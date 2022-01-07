package com.kkoz.sadogorod.filters;

import com.kkoz.sadogorod.entities.uzer.Uzer;
import com.kkoz.sadogorod.entities.uzer.UzerRole;
import com.kkoz.sadogorod.entities.uzer.Uzer_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SpecUzer {

    public Specification<Uzer> getFirstNameFilter(String firstName) {
        return (root, criteriaQuery, criteriaBuilder) -> (
                criteriaBuilder.like(criteriaBuilder.lower(root.get(Uzer_.FIRST_NAME)),
                        "%" + firstName.toLowerCase() + "%"
                )
        );
    }

    public Specification<Uzer> getLastNameFilter(String lastName) {
        return (root, criteriaQuery, criteriaBuilder) -> (
                criteriaBuilder.like(criteriaBuilder.lower(root.get(Uzer_.LAST_NAME)),
                        "%" + lastName.toLowerCase() + "%"
                )
        );
    }

    public Specification<Uzer> getPatronymicNameFilter(String patronymicName) {
        return (root, criteriaQuery, criteriaBuilder) -> (
                criteriaBuilder.like(criteriaBuilder.lower(root.get(Uzer_.PATRONYMIC_NAME)),
                        "%" + patronymicName.toLowerCase() + "%"
                )
        );
    }

    public Specification<Uzer> getRoleFilter(UzerRole role) {
        return (root, criteriaQuery, criteriaBuilder) -> (
                criteriaBuilder.isMember(role, root.get(Uzer_.ROLE))
        );
    }

    public Specification<Uzer> getIsActiveFilter(Boolean isActive) {
        return (root, criteriaQuery, criteriaBuilder) -> (
                criteriaBuilder.equal(root.get(Uzer_.IS_ACTIVE), isActive)
        );
    }
}
