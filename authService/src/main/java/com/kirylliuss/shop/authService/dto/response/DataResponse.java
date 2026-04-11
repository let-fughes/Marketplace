package com.kirylliuss.shop.authService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataResponse {
    private String login;
    private String hashedPassword;
    private String access;
}
