package com.kirylliuss.shop.userService.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "Login is mandatory.")
    @Size(max = 100, message = "Max login size is 100.")
    private String login;

    @NotBlank(message = "First name is mandatory.")
    @Size(max = 50, message = "First name should be less than 50.")
    private String firstName;

    @NotBlank(message = "Last name is mandatory.")
    @Size(max = 50, message = "Last name should be less than 50.")
    private String lastName;

    @Past(message = "Birth date should be in the past.")
    private LocalDate birthDate;

    @NotBlank(message = "Email is mandatory.")
    @Size(max = 100, message = "Max email size is 100.")
    @Email(message = "Email should be valid.")
    @Schema(description = "User's email validation", example = "john@example.com")
    private String email;

    @NotBlank(message = "Phone is mandatory.")
    @Size(message = "Max phone size is 20.", max = 20)
    private String phoneNumber;

    @NotBlank(message = "Role is mandatory.")
    private String access;

    private String profilePhotoUrl;

    private LocalDateTime createdAt;
}
