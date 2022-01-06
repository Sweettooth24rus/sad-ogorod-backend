package com.kkoz.sadogorod.controls.exceptions;

public class UniqueUzerException extends RuntimeException {
    public UniqueUzerException(String uniqueField) {
        super("Пользователь с таким " + uniqueField + " уже существует");
    }
}
