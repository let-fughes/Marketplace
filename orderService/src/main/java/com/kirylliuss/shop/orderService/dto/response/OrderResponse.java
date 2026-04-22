package com.kirylliuss.shop.orderService.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Long userId;
    private String status;
    private LocalDateTime creationDate;
    private List<OrderItemResponse> items;
    private BigDecimal totalPrice;
}