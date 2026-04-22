package com.kirylliuss.shop.orderService.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends GlobalException {
    public OrderNotFoundException(String msg, HttpStatus httpStatus) {
        super(msg, httpStatus);
    }

    public OrderNotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }
}
