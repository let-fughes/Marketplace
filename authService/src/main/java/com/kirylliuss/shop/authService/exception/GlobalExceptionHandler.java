package com.kirylliuss.shop.authService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserInDataNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserInDataNptFoundExceptionHandler(UserInDataNotFoundException ex){
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
