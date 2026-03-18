package com.streambite.order.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;
    private String customerId;
    private String itemName;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    // ── Getters ──────────────────────────────────
    public String getOrderId()     { return orderId; }
    public String getCustomerId()  { return customerId; }
    public String getItemName()    { return itemName; }
    public int getQuantity()       { return quantity; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt()  { return createdAt; }
    public Instant getUpdatedAt()  { return updatedAt; }

    // ── Setters ──────────────────────────────────
    public void setOrderId(String orderId)         { this.orderId = orderId; }
    public void setCustomerId(String customerId)   { this.customerId = customerId; }
    public void setItemName(String itemName)       { this.itemName = itemName; }
    public void setQuantity(int quantity)          { this.quantity = quantity; }
    public void setStatus(OrderStatus status)      { this.status = status; }
    public void setCreatedAt(Instant createdAt)    { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt)    { this.updatedAt = updatedAt; }
}