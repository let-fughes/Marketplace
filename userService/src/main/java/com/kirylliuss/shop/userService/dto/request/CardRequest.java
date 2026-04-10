package com.kirylliuss.shop.userService.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CardRequest {

    @NotNull(message = "User Id is mandatory.")
    private Long userId;

    @NotBlank(message = "Card number is mandatory.")
    @Size(min = 16, max = 20, message = "Card number should be from 16 to 20.")
    private String number;

    @NotBlank(message = "Holder is mandatory.")
    @Size(max = 100, message = "Max holder length is 100.")
    private String holder;

    @Future(message = "Expiration date must be in the future.")
    private LocalDate expirationDate;
}
