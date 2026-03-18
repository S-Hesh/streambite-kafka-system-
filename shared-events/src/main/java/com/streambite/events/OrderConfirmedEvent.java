package com.streambite.events;

import java.util.UUID;

public class OrderConfirmedEvent {

    private String eventId;
    private String orderId;
    private String customerId;
    private String itemName;
    private int    quantity;
    private long   confirmedAt;

    public OrderConfirmedEvent() {}

    public OrderConfirmedEvent(String orderId, String customerId,
                               String itemName, int quantity) {
        this.eventId      = UUID.randomUUID().toString();
        this.orderId      = orderId;
        this.customerId   = customerId;
        this.itemName     = itemName;
        this.quantity     = quantity;
        this.confirmedAt  = System.currentTimeMillis();
    }

    public String getEventId()     { return eventId; }
    public String getOrderId()     { return orderId; }
    public String getCustomerId()  { return customerId; }
    public String getItemName()    { return itemName; }
    public int    getQuantity()    { return quantity; }
    public long   getConfirmedAt() { return confirmedAt; }

    public void setEventId(String eventId)         { this.eventId = eventId; }
    public void setOrderId(String orderId)         { this.orderId = orderId; }
    public void setCustomerId(String customerId)   { this.customerId = customerId; }
    public void setItemName(String itemName)       { this.itemName = itemName; }
    public void setQuantity(int quantity)          { this.quantity = quantity; }
    public void setConfirmedAt(long confirmedAt)   { this.confirmedAt = confirmedAt; }
}