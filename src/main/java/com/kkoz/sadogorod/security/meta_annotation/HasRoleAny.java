package com.kkoz.sadogorod.security.meta_annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR', 'SENIOR_OPERATOR', 'SENATE', 'APPLICANT')")
public @interface HasRoleAny {
}
