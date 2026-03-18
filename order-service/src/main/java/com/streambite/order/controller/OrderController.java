package com.streambite.order.controller;

import com.streambite.order.model.Order;
import com.streambite.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> placeOrder(
            @RequestBody OrderRequest request) {

        String orderId = orderService.placeOrder(
                request.customerId(),
                request.itemName(),
                request.quantity()
        );

        return ResponseEntity.accepted().body(Map.of(
                "orderId", orderId,
                "status",  "PENDING",
                "message", "Order received - processing asynchronously"
        ));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        return orderService.getOrder(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public record OrderRequest(String customerId, String itemName, int quantity) {}
}