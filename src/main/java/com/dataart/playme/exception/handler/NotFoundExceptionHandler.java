package com.dataart.playme.exception.handler;

import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class NotFoundExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchRecordException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchRecordExceptions(NoSuchRecordException exception) {
        return new ResponseEntity<>(Map.of(Constants.MESSAGE_PROPERTY, exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
