package com.kkoz.sadogorod.controls.exceptions;

public class CannotDeleteFileException extends RuntimeException {
    public CannotDeleteFileException(String file) {
        super("Невозможно удалить файл [" + file + "]");
    }
}