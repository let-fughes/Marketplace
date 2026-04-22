package com.kirylliuss.shop.orderService.exception;

import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends GlobalException{
    public ItemNotFoundException(String msg, HttpStatus httpStatus) {
        super(msg, httpStatus);
    }

    public ItemNotFoundException(String msg){
        super(msg, HttpStatus.NOT_FOUND);
    }
}
