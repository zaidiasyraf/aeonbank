package com.zaidi.aeonbank.exceptionhandler;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse
                        .builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = {IllegalStateException.class,})
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse
                        .builder(ex, HttpStatus.FORBIDDEN, ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = {EntityExistsException.class})
    public ResponseEntity<ErrorResponse> handleEntityExist(EntityExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse
                        .builder(ex, HttpStatus.FORBIDDEN, ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse
                        .builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                        .build());
    }

}
