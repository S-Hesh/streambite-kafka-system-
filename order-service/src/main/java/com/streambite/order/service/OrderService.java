package com.streambite.order.service;

import com.streambite.events.OrderCreatedEvent;
import com.streambite.order.model.Order;
import com.streambite.order.model.OrderStatus;
import com.streambite.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderService(OrderRepository orderRepository,
                        KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate   = kafkaTemplate;
    }

    @Transactional
    public String placeOrder(String customerId, String itemName, int quantity) {

        // 1. Save order as PENDING immediately
        String orderId = UUID.randomUUID().toString();
        Order order = new Order();
        order.setOrderId(orderId);
        order.setCustomerId(customerId);
        order.setItemName(itemName);
        order.setQuantity(quantity);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(Instant.now());
        orderRepository.save(order);
        log.info("Saved order {} as PENDING", orderId);

        // 2. Publish to Kafka — key = customerId (same customer = same partition)
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, customerId, itemName, quantity);

        kafkaTemplate.send("order.created", customerId, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish order {}: {}", orderId, ex.getMessage());
                    } else {
                        log.info("Published order {} → partition={} offset={}",
                                orderId,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });

        return orderId;
    }

    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional
    public void updateStatus(String orderId, OrderStatus status) {
        orderRepository.findById(orderId).ifPresent(o -> {
            o.setStatus(status);
            o.setUpdatedAt(Instant.now());
            orderRepository.save(o);
            log.info("Order {} status updated to {}", orderId, status);
        });
    }
}