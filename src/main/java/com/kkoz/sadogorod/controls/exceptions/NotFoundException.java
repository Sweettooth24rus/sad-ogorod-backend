package com.kkoz.sadogorod.controls.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Integer id, String value) {
        super("Не могу найти [" + value + "] с id [" + id + "]");
    }

    public NotFoundException(String key, String value) {
        super("Не могу найти [" + value + "] c [" + key + "]");
    }

    public NotFoundException(String entity, String parameter, String parameterValue) {
        super("Не могу найти [" + entity + "] с " + parameter + " [" + parameterValue + "]");
    }
}
