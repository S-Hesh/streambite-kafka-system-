package com.streambite.notification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "order_records")
public class OrderRecord {

    @Id
    private String orderId;

    private String customerId;
    private String itemName;
    private String status;    // "CONFIRMED" or "FAILED"
    private String reason;    // null for confirmed, "OUT_OF_STOCK" for failed
    private Instant updatedAt;

    // ── No-arg constructor required by JPA ───────────────────────────
    public OrderRecord() {}

    public OrderRecord(String orderId,
                       String customerId,
                       String itemName,
                       String status,
                       String reason,
                       Instant updatedAt) {
        this.orderId     = orderId;
        this.customerId  = customerId;
        this.itemName    = itemName;
        this.status      = status;
        this.reason      = reason;
        this.updatedAt   = updatedAt;
    }

    // ── Getters ──────────────────────────────────────────────────────
    public String getOrderId()    { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getItemName()   { return itemName; }
    public String getStatus()     { return status; }
    public String getReason()     { return reason; }
    public Instant getUpdatedAt() { return updatedAt; }

    // ── Setters ──────────────────────────────────────────────────────
    public void setOrderId(String orderId)       { this.orderId = orderId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setItemName(String itemName)     { this.itemName = itemName; }
    public void setStatus(String status)         { this.status = status; }
    public void setReason(String reason)         { this.reason = reason; }
    public void setUpdatedAt(Instant updatedAt)  { this.updatedAt = updatedAt; }
}