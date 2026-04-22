package com.kirylliuss.shop.orderService.exception;

import org.springframework.http.HttpStatus;

public class OrderItemNotFoundException extends GlobalException{
    public OrderItemNotFoundException(String msg, HttpStatus httpStatus) {
        super(msg, httpStatus);
    }

    public OrderItemNotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }
}
