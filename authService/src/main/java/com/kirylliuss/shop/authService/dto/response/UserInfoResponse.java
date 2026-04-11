package com.kirylliuss.shop.authService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private Long userId;
    private String firstName;
    private String login;
    private String access;
}
