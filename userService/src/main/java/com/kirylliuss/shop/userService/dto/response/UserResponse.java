package com.kirylliuss.shop.userService.dto.response;

import com.kirylliuss.shop.userService.model.Card;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class UserResponse {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private String role;
    private LocalDateTime createdAt;
    private List<Card> cards;
}
