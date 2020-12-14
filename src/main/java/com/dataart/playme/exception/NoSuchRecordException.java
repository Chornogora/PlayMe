package com.dataart.playme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchRecordException extends RuntimeException {

    public NoSuchRecordException(String message) {
        super(message);
    }
}
