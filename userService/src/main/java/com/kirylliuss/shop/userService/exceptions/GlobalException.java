package com.kirylliuss.shop.userService.exceptions;

import org.springframework.http.HttpStatus;

public class GlobalException extends RuntimeException{
    private HttpStatus status;

    public GlobalException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return status;
    }
}
