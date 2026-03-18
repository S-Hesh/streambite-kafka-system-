package com.streambite.inventory.consumer;

import com.streambite.events.OrderConfirmedEvent;
import com.streambite.events.OrderCreatedEvent;
import com.streambite.events.OrderFailedEvent;
import com.streambite.inventory.idempotency.ProcessedEventStore;
import com.streambite.inventory.stock.StockRegistry;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventConsumer.class);

    private final StockRegistry stock;
    private final ProcessedEventStore idempotency;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryEventConsumer(StockRegistry stock,
                                  ProcessedEventStore idempotency,
                                  KafkaTemplate<String, Object> kafkaTemplate) {
        this.stock         = stock;
        this.idempotency   = idempotency;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order.created", groupId = "inventory-service")
    public void handle(
            ConsumerRecord<String, OrderCreatedEvent> record,
            Acknowledgment ack) {

        OrderCreatedEvent event = record.value();

        log.info("Received → order={} item={} qty={} partition={} offset={}",
                event.getOrderId(),
                event.getItemName(),
                event.getQuantity(),
                record.partition(),
                record.offset());

        try {
            // ── IDEMPOTENCY CHECK ─────────────────────────────────────
            if (idempotency.isAlreadyProcessed(event.getEventId())) {
                log.warn("Duplicate event {} — skipping", event.getEventId());
                ack.acknowledge();
                return;
            }

            // ── STOCK CHECK ───────────────────────────────────────────
            boolean reserved = stock.deduct(event.getItemName(), event.getQuantity());

            if (reserved) {
                kafkaTemplate.send(
                        "order.confirmed",
                        event.getCustomerId(),
                        new OrderConfirmedEvent(
                                event.getOrderId(),
                                event.getCustomerId(),
                                event.getItemName(),
                                event.getQuantity()
                        )
                );
                log.info("CONFIRMED order={}", event.getOrderId());
            } else {
                kafkaTemplate.send(
                        "order.failed",
                        event.getCustomerId(),
                        new OrderFailedEvent(
                                event.getOrderId(),
                                event.getCustomerId(),
                                "OUT_OF_STOCK"
                        )
                );
                log.warn("FAILED order={} reason=OUT_OF_STOCK", event.getOrderId());
            }

            // ── MARK PROCESSED ────────────────────────────────────────
            idempotency.markProcessed(event.getEventId());

            // ── COMMIT OFFSET ─────────────────────────────────────────
            // Only commit AFTER successful processing.
            // If the app crashes before this line, Kafka redelivers the
            // event and the idempotency check above handles it safely.
            ack.acknowledge();
            log.info("Offset committed for order={}", event.getOrderId());

        } catch (Exception ex) {
            // Do NOT acknowledge — Kafka will redeliver this message.
            // Log it so you can see what went wrong.
            log.error("Processing failed for order={} — will redeliver. Error: {}",
                    event.getOrderId(), ex.getMessage(), ex);
        }
    }
}