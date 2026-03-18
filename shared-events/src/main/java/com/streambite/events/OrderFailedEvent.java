package com.streambite.events;

import java.util.UUID;

public class OrderFailedEvent {

    private String eventId;
    private String orderId;
    private String customerId;
    private String reason;
    private long   failedAt;

    public OrderFailedEvent() {}

    public OrderFailedEvent(String orderId, String customerId, String reason) {
        this.eventId    = UUID.randomUUID().toString();
        this.orderId    = orderId;
        this.customerId = customerId;
        this.reason     = reason;
        this.failedAt   = System.currentTimeMillis();
    }

    public String getEventId()    { return eventId; }
    public String getOrderId()    { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getReason()     { return reason; }
    public long   getFailedAt()   { return failedAt; }

    public void setEventId(String eventId)       { this.eventId = eventId; }
    public void setOrderId(String orderId)       { this.orderId = orderId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setReason(String reason)         { this.reason = reason; }
    public void setFailedAt(long failedAt)       { this.failedAt = failedAt; }
}