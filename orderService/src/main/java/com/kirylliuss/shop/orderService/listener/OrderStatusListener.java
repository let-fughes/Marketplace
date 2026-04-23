package com.kirylliuss.shop.orderService.listener;

import com.kirylliuss.shop.orderService.dto.PaymentEvent;
import com.kirylliuss.shop.orderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderStatusListener {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "payment-events", groupId = "order-status-group")
    public void listenPaymentEvents(PaymentEvent event) {
        log.info("Kafka: Received payment event for order ID: {} with status: {}",
                event.orderId(), event.status());

        orderRepository.findById(event.orderId()).ifPresentOrElse(order -> {
            order.setStatus(event.status());
            orderRepository.save(order);
            log.info("Order {} status updated to {}", event.orderId(), event.status());
        }, () -> log.error("Order with ID {} not found!", event.orderId()));
    }
}