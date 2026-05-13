package com.kirylliuss.shop.orderService.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Long userId;
}