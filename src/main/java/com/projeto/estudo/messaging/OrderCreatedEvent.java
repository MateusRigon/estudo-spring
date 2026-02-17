package com.projeto.estudo.messaging;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderCreatedEvent {

    private Long orderId;
    private Integer clientId;
    private LocalDateTime createdAt;
    private List<OrderItemEvent> items;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId, Integer clientId, LocalDateTime createdAt, List<OrderItemEvent> items) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemEvent> getItems() {
        return items;
    }

    public void setItems(List<OrderItemEvent> items) {
        this.items = items;
    }

    public static class OrderItemEvent {
        private Long productId;
        private Integer quantity;
        private BigDecimal unitPrice;

        public OrderItemEvent() {
        }

        public OrderItemEvent(Long productId, Integer quantity, BigDecimal unitPrice) {
            this.productId = productId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }
    }
}
