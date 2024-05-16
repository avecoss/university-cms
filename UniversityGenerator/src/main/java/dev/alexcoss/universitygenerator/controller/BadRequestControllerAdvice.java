package dev.alexcoss.universitygenerator.controller;

import dev.alexcoss.universitygenerator.util.ErrorDetails;
import dev.alexcoss.universitygenerator.util.exception.FileReadException;
import dev.alexcoss.universitygenerator.util.exception.InvalidRoleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;

@Slf4j
@ControllerAdvice
public class BadRequestControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errors = bindingResult.getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorDetails("Invalid Data", errors, System.currentTimeMillis()));
    }

    @ExceptionHandler(FileReadException.class)
    public ResponseEntity<ErrorDetails> handleFileReadException(FileReadException ex) {
        log.error("File read error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorDetails("File read error", Collections.singletonList(ex.getMessage()), System.currentTimeMillis()));
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ErrorDetails> handleInvalidRoleException(InvalidRoleException ex) {
        log.error("Invalid role error", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorDetails("Invalid role", Collections.singletonList(ex.getMessage()), System.currentTimeMillis()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorDetails("Internal Server Error", Collections.singletonList(ex.getMessage()), System.currentTimeMillis()));
    }
}
