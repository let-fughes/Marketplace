package com.kirylliuss.shop.orderService.exception;

import org.springframework.http.HttpStatus;

public class GlobalException extends RuntimeException{

    private HttpStatus httpStatus;

    public GlobalException(String msg, HttpStatus httpStatus){
        super(msg);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}
