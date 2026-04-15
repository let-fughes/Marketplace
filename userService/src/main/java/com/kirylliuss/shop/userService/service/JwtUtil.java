package com.kirylliuss.shop.userService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    public String generateAccessToken(String login, String access){
        return generateToken(login, accessTokenExpiration, access);
    }

    public String generateRefreshToken(String login, String access){
        return generateToken(login, refreshTokenExpiration, access);
    }

    private java.security.Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(String login, Long expiration, String access){
        return Jwts.builder()
                .setSubject(login)
                .claim("access", access)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey()) // ИСПРАВЛЕНО
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey()) // ИСПРАВЛЕНО
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Date getExpirationDateFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey()) // ИСПРАВЛЕНО
                .build()
                .parseClaimsJws(token) // Используй Jws для подписанных токенов
                .getBody();
        return claims.getExpiration();
    }

    public boolean isRefreshToken(String refreshToken){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSigningKey()) // ИСПРАВЛЕНО
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            Date expiration = claims.getExpiration();
            long tokenLifetime = expiration.getTime() - claims.getIssuedAt().getTime();
            return tokenLifetime > accessTokenExpiration;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}
