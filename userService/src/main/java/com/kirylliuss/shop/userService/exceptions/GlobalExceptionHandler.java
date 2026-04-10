package com.kirylliuss.shop.userService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCardNotFoundException(CardNotFoundException ex){
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", ex.getStatus().name());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex){
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", ex.getStatus().name());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(GlobalException ex){
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", ex.getStatus().name());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex){
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
