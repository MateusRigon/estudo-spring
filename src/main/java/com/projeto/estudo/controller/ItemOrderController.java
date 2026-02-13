package com.projeto.estudo.controller;

import com.projeto.estudo.dto.ApiResponse;
import com.projeto.estudo.model.ItemOrder;
import com.projeto.estudo.service.ItemOrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class ItemOrderController {

    private final ItemOrderService itemOrderService;

    public ItemOrderController(ItemOrderService itemOrderService) {
        this.itemOrderService = itemOrderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemOrder>>> getAllItems() {
        List<ItemOrder> items = itemOrderService.getAllItems();
        return ResponseEntity.ok(ApiResponse.success("Order items retrieved successfully.", items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemOrder>> getItemById(@PathVariable Long id) {
        ItemOrder item = itemOrderService.getItemById(id);
        return ResponseEntity.ok(ApiResponse.success("Order item retrieved successfully.", item));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<ItemOrder>>> getItemsByOrderId(@PathVariable Long orderId) {
        List<ItemOrder> items = itemOrderService.getItemsByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order items retrieved successfully.", items));
    }

    @PostMapping("/order/{orderId}/product/{productId}")
    public ResponseEntity<ApiResponse<ItemOrder>> createItem(
            @PathVariable Long orderId,
            @PathVariable Long productId,
            @Valid @RequestBody ItemOrder payload
    ) {
        ItemOrder item = itemOrderService.createItem(orderId, productId, payload);
        return ResponseEntity.ok(ApiResponse.success("Order item created successfully.", item));
    }

    @PutMapping("/{id}/product/{productId}")
    public ResponseEntity<ApiResponse<ItemOrder>> updateItem(
            @PathVariable Long id,
            @PathVariable Long productId,
            @Valid @RequestBody ItemOrder payload
    ) {
        ItemOrder item = itemOrderService.updateItem(id, productId, payload);
        return ResponseEntity.ok(ApiResponse.success("Order item updated successfully.", item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        itemOrderService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success("Order item deleted successfully.", null));
    }
}
