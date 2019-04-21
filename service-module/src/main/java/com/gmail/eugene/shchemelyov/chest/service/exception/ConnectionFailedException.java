package com.gmail.eugene.shchemelyov.chest.service.exception;

public class ConnectionFailedException extends RuntimeException {
    public ConnectionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
