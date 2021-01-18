package com.dataart.playme.exception.handler;

import com.dataart.playme.util.Constants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.ws.rs.BadRequestException;
import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class BadRequestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> processError(error, errors));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleConverterExceptions(BadRequestException exception) {
        if (exception.getResponse() != null && exception.getResponse().getEntity() != null) {
            return new ResponseEntity<>(exception.getResponse().getEntity(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Map.of(Constants.MESSAGE_PROPERTY, exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private void processError(ObjectError error, Map<String, String> errors) {
        String errorMessage = error.getDefaultMessage();
        if (error instanceof FieldError) {
            String fieldName = ((FieldError) error).getField();
            errors.put(fieldName, errorMessage);
        } else {
            String objectName = error.getObjectName();
            errors.put(objectName, errorMessage);
        }
    }
}
