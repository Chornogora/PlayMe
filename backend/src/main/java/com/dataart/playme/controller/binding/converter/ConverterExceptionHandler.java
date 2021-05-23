package com.dataart.playme.controller.binding.converter;

import com.dataart.playme.exception.NoSuchRecordException;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ConverterExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleConverterExceptions(MethodArgumentTypeMismatchException exception) {
        if(hasNoSuchRecordExceptionCause(exception)){
            return new ResponseEntity<>("Not found: " + exception.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Bad request: " + exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private boolean hasNoSuchRecordExceptionCause(MethodArgumentTypeMismatchException root) {
        Throwable cause = root.getCause();
        while(cause != null){
            if(cause instanceof NoSuchRecordException){
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}
