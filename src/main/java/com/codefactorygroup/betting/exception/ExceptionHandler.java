package com.codefactorygroup.betting.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchEntityExistsException.class)
    public ResponseEntity<ExceptionDTO> handleNoSuchEntityExistsException(NoSuchEntityExistsException ex,
                                                                          WebRequest request) {
        return new ResponseEntity<>(new ExceptionDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleEntityAlreadyExists(EntityAlreadyExistsException ex,
                                                                  WebRequest request) {
        return new ResponseEntity<>(new ExceptionDTO(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handleConstraintViolationException(ConstraintViolationException ex,
                                                                  WebRequest request) {
        return new ResponseEntity<>(new ExceptionDTO(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value()), HttpStatus.METHOD_NOT_ALLOWED);
    }
}
