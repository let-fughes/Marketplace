package com.kirylliuss.shop.authService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataRequest {

    @NotBlank(message = "Login is mandatory.")
    @Size(max = 100, message = "Maximum login size is 100.")
    private String login;

    @NotBlank(message = "Password is mandatory.")
    @Size(max = 100, message = "Maximum hashed password size is 100.")
    private String hashedPassword;

    @NotBlank(message = "Access is mandatory.")
    @Size(max = 100, message = "Maximum access size is 10.")
    private String access;
}
