package com.kirylliuss.shop.authService.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends GlobalException{

    public UserAlreadyExistsException(HttpStatus status, String message) {
        super(status, message);
    }

    public UserAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
