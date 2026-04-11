package com.kirylliuss.shop.authService.exception;

import org.springframework.http.HttpStatus;

public class UserInDataNotFoundException extends GlobalException{
    public UserInDataNotFoundException(String message){
        super(HttpStatus.NOT_FOUND, message);
    }

    public UserInDataNotFoundException(Long id){
        super(HttpStatus.NOT_FOUND, "User with id: " + id + " not found.");
    }
}
