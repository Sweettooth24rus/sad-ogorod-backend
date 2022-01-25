package com.kkoz.sadogorod.controls.exceptions;

import java.util.Set;

public class FileExtensionException extends RuntimeException {
    public FileExtensionException(String filename) {
        super("Файл [" + filename + "] имеет недопустимое расширение");
    }

    public FileExtensionException(Set<String> invalidFiles) {
        super("Файл(ы) " + invalidFiles + " имеет(ют) недопустимое расширение");
    }
}
