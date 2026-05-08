package com.kirylliuss.shop.authService.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserCreateRequest {

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

    private LocalDateTime createdAt;

    public UserCreateRequest(String login) {
        this.login = login;
        this.firstName = "User";
        this.lastName = "User";
        this.email = login + "@example.com";
        this.phoneNumber = "000000";
        this.access = "USER";
        this.birthDate = LocalDate.now().minusYears(18);
        this.createdAt = LocalDateTime.now();
    }

    public UserCreateRequest(String login, String name, String surname, LocalDate birthDate, String email, String number){
        this.login = login;
        this.firstName = name;
        this.lastName = surname;
        this.email = email;
        this.phoneNumber = number;
        this.access = "USER";
        this.birthDate = birthDate;
        this.createdAt = LocalDateTime.now();;
    }
}
