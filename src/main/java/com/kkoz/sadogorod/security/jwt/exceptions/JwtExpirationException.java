package com.kkoz.sadogorod.security.jwt.exceptions;

public class JwtExpirationException extends RuntimeException {
    public JwtExpirationException(String message) {
        super(message);
    }
}
