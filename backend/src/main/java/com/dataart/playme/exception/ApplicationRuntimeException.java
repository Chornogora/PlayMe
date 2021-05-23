package com.dataart.playme.exception;

public class ApplicationRuntimeException extends RuntimeException {

    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationRuntimeException(String message) {
        super(message);
    }
}
