package com.gmail.eugene.shchemelyov.chest.service.exception;

public class FileNotExistException extends RuntimeException {
    public FileNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
