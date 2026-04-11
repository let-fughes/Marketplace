package com.kirylliuss.shop.authService.client;

import com.kirylliuss.shop.authService.dto.request.UserCreateRequest;
import com.kirylliuss.shop.authService.dto.response.UserCreationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-service",
        url = "${user-service.url}",
        path = "/v1/api"
)
public interface UserServiceClient {
    @PostMapping("/users")
    ResponseEntity<UserCreationResponse> createUser(@RequestBody UserCreateRequest request);
}
