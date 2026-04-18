package com.kirylliuss.shop.orderService.mapper;

import com.kirylliuss.shop.orderService.dto.response.OrderItemResponse;
import com.kirylliuss.shop.orderService.dto.response.OrderResponse;
import com.kirylliuss.shop.orderService.model.Order;
import com.kirylliuss.shop.orderService.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "totalPrice", source = "orderItems", qualifiedByName = "calculateTotal")
    OrderResponse toResponse(Order order);

    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "itemName", source = "item.name")
    @Mapping(target = "price", source = "item.price")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    @Named("calculateTotal")
    default BigDecimal calculateTotal(List<OrderItem> orderItems) {
        if (orderItems == null) return BigDecimal.ZERO;
        return orderItems.stream()
                .map(oi -> oi.getItem().getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}