package com.projeto.estudo.controller;

import com.projeto.estudo.dto.ApiResponse;
import com.projeto.estudo.model.Order;
import com.projeto.estudo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully.", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully.", order));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByClientId(@PathVariable int clientId) {
        List<Order> orders = orderService.getOrdersByClientId(clientId);
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully.", orders));
    }

    @PostMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<Order>> createOrder(
            @PathVariable int clientId,
            @Valid @RequestBody Order payload
    ) {
        Order order = orderService.createOrder(clientId, payload);
        return ResponseEntity.ok(ApiResponse.success("Order created successfully.", order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody Order payload
    ) {
        Order order = orderService.updateOrder(id, payload);
        return ResponseEntity.ok(ApiResponse.success("Order updated successfully.", order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order deleted successfully.", null));
    }
}
