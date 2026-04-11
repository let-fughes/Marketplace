package com.kirylliuss.shop.authService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Login is mandatory.")
    @Size(max = 100, message = "Maximum login size is 100.")
    private String login;

    @NotBlank(message = "Password is mandatory.")
    @Size(max = 100, message = "Maximum hashed password size is 100.")
    private String password;
}
