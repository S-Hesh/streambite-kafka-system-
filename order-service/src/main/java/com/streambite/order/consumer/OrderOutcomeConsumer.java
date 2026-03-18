package com.streambite.order.consumer;

import com.streambite.events.OrderConfirmedEvent;
import com.streambite.events.OrderFailedEvent;
import com.streambite.order.model.OrderStatus;
import com.streambite.order.service.OrderService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class OrderOutcomeConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderOutcomeConsumer.class);

    private final OrderService orderService;

    public OrderOutcomeConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "order.confirmed", groupId = "order-service-updater")
    public void onConfirmed(
            ConsumerRecord<String, OrderConfirmedEvent> record,
            Acknowledgment ack) {

        String orderId = record.value().getOrderId();
        log.info("Received CONFIRMED for order {}", orderId);
        orderService.updateStatus(orderId, OrderStatus.CONFIRMED);
        ack.acknowledge();
    }

    @KafkaListener(topics = "order.failed", groupId = "order-service-updater")
    public void onFailed(
            ConsumerRecord<String, OrderFailedEvent> record,
            Acknowledgment ack) {

        String orderId = record.value().getOrderId();
        log.info("Received FAILED for order {}", orderId);
        orderService.updateStatus(orderId, OrderStatus.FAILED);
        ack.acknowledge();
    }
}