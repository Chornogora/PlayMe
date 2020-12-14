package com.dataart.playme.exception.handler;

import com.dataart.playme.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class SecurityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsExceptions(BadCredentialsException exception) {
        return new ResponseEntity<>(Map.of(Constants.MESSAGE_PROPERTY, exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
