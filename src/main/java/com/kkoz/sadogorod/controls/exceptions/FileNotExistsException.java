package com.kkoz.sadogorod.controls.exceptions;

public class FileNotExistsException extends RuntimeException {
    public FileNotExistsException(String file) {
        super("Такого файла [" + file + "] не существует");
    }
}