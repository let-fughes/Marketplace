package com.kirylliuss.shop.authService.exception;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends GlobalException{
    public InvalidRefreshTokenException(HttpStatus status, String message) {
        super(status, message);
    }

    public InvalidRefreshTokenException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
