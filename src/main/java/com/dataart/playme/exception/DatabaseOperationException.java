package com.dataart.playme.exception;

public class DatabaseOperationException extends RuntimeException {

    public DatabaseOperationException(String message, Throwable cause){
        super(message, cause);
    }
}
