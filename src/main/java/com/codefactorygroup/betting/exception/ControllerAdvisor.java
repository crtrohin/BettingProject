package com.codefactorygroup.betting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchParticipantExistsException.class)
    public ResponseEntity<Object> handleNoSuchParticipantExistsException(NoSuchParticipantExistsException ex,
                                                                         WebRequest request) {
        // The LinkedHashMap implements ordered maps,
        // while the HashMap class generally implement unordered maps.
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Participant not found.");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ParticipantAlreadyExistsException.class)
    public ResponseEntity<Object> handleParticipantAlreadyExists(ParticipantAlreadyExistsException ex,
                                                                 WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Participant already exists.");

        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

}
