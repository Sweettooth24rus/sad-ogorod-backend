package com.kkoz.sadogorod.security.jwt.exceptions;

public class ResourceJwtValidationException extends RuntimeException {
    public ResourceJwtValidationException() {
        super("Предъявленный resourceJwt не прошел проверку для доступа к ресурсу");
    }
}
