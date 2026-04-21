package com.kirylliuss.shop.orderService.client;

import com.kirylliuss.shop.orderService.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "${user-service.url}",
        path = "/v1/api/users"
)
public interface UserServiceClient {
    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);
}
