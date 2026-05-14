package com.kirylliuss.shop.orderService.client;

import com.kirylliuss.shop.orderService.config.FeignConfig;
import com.kirylliuss.shop.orderService.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@FeignClient(
        name = "user-service",
        url = "${user-service.url}",
        path = "/v1/api/users",
        configuration = FeignConfig.class
)
public interface UserServiceClient {
    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);

    @GetMapping("/profileByLogin")
    UserResponse getProfile(@RequestParam("login") String login);
}
