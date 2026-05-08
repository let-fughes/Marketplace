package com.kirylliuss.shop.authService.service;

import com.kirylliuss.shop.authService.Repository.DataRepository;
import com.kirylliuss.shop.authService.client.UserServiceClient;
import com.kirylliuss.shop.authService.dto.request.RegisterRequest;
import com.kirylliuss.shop.authService.dto.request.UserCreateRequest;
import com.kirylliuss.shop.authService.dto.response.TokenResponse;
import com.kirylliuss.shop.authService.exception.InvalidRefreshTokenException;
import com.kirylliuss.shop.authService.exception.UserAlreadyExistsException;
import com.kirylliuss.shop.authService.exception.UserInDataNotFoundException;
import com.kirylliuss.shop.authService.mapper.DataMapper;
import com.kirylliuss.shop.authService.model.Data;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final DataRepository dataRepository;
    private final JwtUtil jwtUtil;
    private final DataMapper mapper; // Если не используется, можно удалить
    private final UserServiceClient client;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequest request, String access) {
        if (dataRepository.existsByLogin(request.getLogin())) {
            throw new UserAlreadyExistsException("User already exists: " + request.getLogin());
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        Data user = new Data(request.getLogin(), hashedPassword, access);
        dataRepository.save(user);

        try {
            UserCreateRequest userRequest = new UserCreateRequest(
                    request.getLogin(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getBirthDate(),
                    request.getEmail(),
                    request.getPhoneNumber()
            );

            userRequest.setAccess(access);

            var response = client.createUser(userRequest);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("User Service returned error: " + response.getStatusCode());
            }
            logger.info("{} registered successfully: {}", access, request.getLogin());

        } catch (Exception e) {
            logger.error("Failed to create user in User Service. Rollback. Error: {}", e.getMessage());
            throw new RuntimeException("External service failure, registration rolled back", e);
        }
    }

    @Transactional
    public void registerUser(RegisterRequest registerRequest) {
        register(registerRequest, "USER");
    }

    @Transactional
    public void registerAdmin(RegisterRequest registerRequest) {
        register(registerRequest, "ADMIN");
    }

    @Transactional
    public TokenResponse authenticate(String login, String password) {
        Data user = dataRepository.findByLogin(login)
                .orElseThrow(() -> {
                    logger.warn("Auth attempt failed: User {} not found", login);
                    return new UserInDataNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            logger.warn("Auth attempt failed: Invalid password for user {}", login);
            throw new AccessDeniedException("Invalid login data");
        }

        String accessToken = jwtUtil.generateAccessToken(login, user.getAccess());
        String refreshToken = jwtUtil.generateRefreshToken(login, user.getAccess());

        logger.info("User {} authenticated successfully", login);
        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new InvalidRefreshTokenException(HttpStatus.BAD_REQUEST, "Refresh token is required");
        }

        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new InvalidRefreshTokenException(HttpStatus.UNAUTHORIZED, "Refresh token is invalid");
            }

            if (!jwtUtil.isRefreshToken(refreshToken)) {
                throw new InvalidRefreshTokenException(HttpStatus.BAD_REQUEST, "Provided token is not a refresh token");
            }

            String login = jwtUtil.getUsernameFromToken(refreshToken);

            Data user = dataRepository.findByLogin(login)
                    .orElseThrow(() -> new UserInDataNotFoundException("User not found during refresh"));

            String newAccessToken = jwtUtil.generateAccessToken(login, user.getAccess());
            String newRefreshToken = jwtUtil.generateRefreshToken(login, user.getAccess());

            logger.info("Tokens refreshed for user: {}", login);
            return new TokenResponse(newAccessToken, newRefreshToken);

        } catch (ExpiredJwtException e) {
            logger.warn("Refresh token expired");
            throw new InvalidRefreshTokenException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        } catch (JwtException e) {
            logger.error("JWT Error during refresh: {}", e.getMessage());
            throw new InvalidRefreshTokenException(HttpStatus.UNAUTHORIZED, "Invalid refresh token: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during refresh: {}", e.getMessage());
            throw new InvalidRefreshTokenException(HttpStatus.INTERNAL_SERVER_ERROR, "Token refresh failed");
        }
    }

    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}