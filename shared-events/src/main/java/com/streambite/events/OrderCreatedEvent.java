package com.streambite.events;

import java.util.UUID;

public class OrderCreatedEvent {

    private String eventId;
    private String orderId;
    private String customerId;
    private String itemName;
    private int    quantity;
    private long   createdAt;   // epoch millis — no Jackson config needed

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(String orderId, String customerId,
                             String itemName, int quantity) {
        this.eventId    = UUID.randomUUID().toString();
        this.orderId    = orderId;
        this.customerId = customerId;
        this.itemName   = itemName;
        this.quantity   = quantity;
        this.createdAt  = System.currentTimeMillis();
    }

    public String getEventId()    { return eventId; }
    public String getOrderId()    { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getItemName()   { return itemName; }
    public int    getQuantity()   { return quantity; }
    public long   getCreatedAt()  { return createdAt; }

    public void setEventId(String eventId)       { this.eventId = eventId; }
    public void setOrderId(String orderId)       { this.orderId = orderId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setItemName(String itemName)     { this.itemName = itemName; }
    public void setQuantity(int quantity)        { this.quantity = quantity; }
    public void setCreatedAt(long createdAt)     { this.createdAt = createdAt; }
}