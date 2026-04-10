package com.kirylliuss.shop.userService.exceptions;

import org.springframework.http.HttpStatus;

public class CardNotFoundException extends GlobalException{
    public CardNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public CardNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Card not found.");
    }

    public CardNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Card with id: " + id + " not found.");
    }
}
