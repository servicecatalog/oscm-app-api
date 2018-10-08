/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;

@ControllerAdvice
@RestController
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleAllExceptions(Exception ex,
            WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(),
                ex.getMessage());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public final ResponseEntity handleObjectNotFoundException(
            ObjectNotFoundException exception, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(),
                exception.getMessage());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity handleValidationException(
            ValidationException exception, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(),
                exception.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        HashMap<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().stream()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(),
                "Validation failed: " + errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
