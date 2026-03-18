package com.streambite.notification.consumer;

import com.streambite.events.OrderConfirmedEvent;
import com.streambite.events.OrderFailedEvent;
import com.streambite.notification.model.OrderRecord;
import com.streambite.notification.repository.OrderRecordRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class NotificationEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);

    private final OrderRecordRepository repository;

    public NotificationEventConsumer(OrderRecordRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "order.confirmed", groupId = "notification-service")
    public void onConfirmed(
            ConsumerRecord<String, OrderConfirmedEvent> record,
            Acknowledgment ack) {

        OrderConfirmedEvent event = record.value();

        // 1. Persist outcome to DB
        OrderRecord orderRecord = new OrderRecord(
                event.getOrderId(),
                event.getCustomerId(),
                event.getItemName(),
                "CONFIRMED",
                null,
                Instant.now()
        );
        repository.save(orderRecord);

        // 2. Simulate notification (in production: SendGrid / Twilio / SES)
        log.info("NOTIFICATION SENT → customer={} | Your order for {} x{} is CONFIRMED!",
                event.getCustomerId(),
                event.getItemName(),
                event.getQuantity());

        ack.acknowledge();
    }

    @KafkaListener(topics = "order.failed", groupId = "notification-service")
    public void onFailed(
            ConsumerRecord<String, OrderFailedEvent> record,
            Acknowledgment ack) {

        OrderFailedEvent event = record.value();

        // 1. Persist outcome to DB
        OrderRecord orderRecord = new OrderRecord(
                event.getOrderId(),
                event.getCustomerId(),
                null,
                "FAILED",
                event.getReason(),
                Instant.now()
        );
        repository.save(orderRecord);

        // 2. Simulate notification
        log.warn("NOTIFICATION SENT → customer={} | Your order FAILED — reason: {}",
                event.getCustomerId(),
                event.getReason());

        ack.acknowledge();
    }
}