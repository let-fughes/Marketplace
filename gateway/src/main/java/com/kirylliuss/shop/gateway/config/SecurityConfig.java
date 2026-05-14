package com.kirylliuss.shop.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String secretKey;

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        // Декодируем из Base64
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);

        // ПРИНУДИТЕЛЬНО создаем SecretKeySpec (никаких Keys.hmacShaKeyFor)
        // Если длина байт != 64, Spring выбросит ошибку при старте - это нам и нужно,
        // чтобы увидеть, что ключ не тот.
        var key = new SecretKeySpec(keyBytes, "HmacSHA512");

        return NimbusReactiveJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    public SecurityWebFilterChain securityFilter(ServerHttpSecurity http) {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/v1/api/auth/**").permitAll()
                        .pathMatchers(org.springframework.http.HttpMethod.OPTIONS).permitAll()
                        .pathMatchers("/v1/api/orders/**").hasAnyRole("USER","ADMIN")
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/v1/api/items/**").permitAll()
                        .pathMatchers(org.springframework.http.HttpMethod.POST, "/v1/api/items/**").hasAnyRole("USER", "ADMIN")
                        .pathMatchers("/v1/api/users/**").hasAnyRole("USER", "ADMIN")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
                "http://127.0.0.1:5500",
                "http://localhost:5500",
                "http://127.0.0.1:9000",
                "http://localhost:9000"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        return jwt -> {
            String role = jwt.getClaimAsString("access");
            Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            return Mono.just(new JwtAuthenticationToken(jwt, authorities));
        };
    }
}