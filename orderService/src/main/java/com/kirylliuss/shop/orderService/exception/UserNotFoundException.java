package com.kirylliuss.shop.orderService.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GlobalException{
    public UserNotFoundException(String msg, HttpStatus httpStatus) {
        super(msg, httpStatus);
    }

    public UserNotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }
}
