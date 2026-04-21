package com.kirylliuss.shop.orderService.dto;

import java.math.BigDecimal;

public record PaymentEvent(
        Long orderId,
        String status,
        BigDecimal amount
) {}
