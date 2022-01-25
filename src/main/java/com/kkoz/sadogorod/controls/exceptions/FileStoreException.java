package com.kkoz.sadogorod.controls.exceptions;

public class FileStoreException extends RuntimeException {
    public FileStoreException() {
        super("Невозможно записать файл на сервер");
    }
}
