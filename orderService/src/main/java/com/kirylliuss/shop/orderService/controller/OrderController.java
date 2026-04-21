package com.kirylliuss.shop.orderService.controller;

import com.kirylliuss.shop.orderService.dto.request.OrderItemRequest;
import com.kirylliuss.shop.orderService.dto.request.OrderRequest;
import com.kirylliuss.shop.orderService.dto.response.OrderItemResponse;
import com.kirylliuss.shop.orderService.dto.response.OrderResponse;
import com.kirylliuss.shop.orderService.model.OrderItem;
import com.kirylliuss.shop.orderService.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id){
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllOrdersByUserId(@PathVariable Long userId){
        List<OrderResponse> responses = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest orderRequest){
        OrderResponse response = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/addToCard")
    public ResponseEntity<?> addToCard(@Valid @RequestBody OrderItemRequest request){
        OrderItemResponse response = orderService.addToCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/deleteFromCard/{id}")
    public ResponseEntity<Void> deleteFromCard(@PathVariable Long id){
        orderService.deleteFromCard(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}