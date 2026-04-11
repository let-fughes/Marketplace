package com.kirylliuss.shop.authService.controller;

import com.kirylliuss.shop.authService.dto.request.LoginRequest;
import com.kirylliuss.shop.authService.dto.request.RegisterRequest;
import com.kirylliuss.shop.authService.dto.request.TokenValidationRequest;
import com.kirylliuss.shop.authService.dto.response.TokenResponse;
import com.kirylliuss.shop.authService.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/api/auth")
@Validated
@Tag(name = "auth-service", description = "Auth's API")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/registerAdmin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid RegisterRequest signUpRequest){
        authService.registerAdmin(signUpRequest);
        return ResponseEntity.ok(Map.of("message", "Admin registered successfully"));
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest signUpRequest){
        authService.registerUser(signUpRequest);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest){
        TokenResponse tokens = authService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateToken(@RequestBody TokenValidationRequest request) {
        boolean isValid = authService.validateToken(request.getToken());
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(@RequestBody TokenValidationRequest tokenValidationRequest){
        TokenResponse tokens = authService.refreshToken(tokenValidationRequest.getToken());
        return ResponseEntity.ok(tokens);
    }
}
