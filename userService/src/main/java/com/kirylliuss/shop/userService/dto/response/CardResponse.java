package com.kirylliuss.shop.userService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CardResponse {
    private Long userId;
    private String number;
    private String holder;
    private LocalDate expirationDate;
}
