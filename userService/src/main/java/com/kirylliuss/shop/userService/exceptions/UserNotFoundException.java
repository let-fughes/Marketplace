package com.kirylliuss.shop.userService.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GlobalException{
    public UserNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User not found.");
    }

    public UserNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "User with id: " + id + " not found.");
    }
}
