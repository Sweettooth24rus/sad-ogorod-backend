package com.kkoz.sadogorod.security.jwt.exceptions;

public class JwtValidationException extends RuntimeException {
    public JwtValidationException(String message) {
        super(message);
    }
}
