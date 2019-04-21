package com.gmail.eugene.shchemelyov.chest.repository.exception;

public class DatabaseException extends RuntimeException{
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
