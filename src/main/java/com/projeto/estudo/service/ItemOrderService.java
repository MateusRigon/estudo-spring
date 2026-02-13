package com.projeto.estudo.service;

import com.projeto.estudo.handler.InvalidRequestException;
import com.projeto.estudo.handler.ResourceNotFoundException;
import com.projeto.estudo.model.ItemOrder;
import com.projeto.estudo.model.Order;
import com.projeto.estudo.model.Product;
import com.projeto.estudo.repository.ItemOrderRepository;
import com.projeto.estudo.repository.OrderRepository;
import com.projeto.estudo.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemOrderService {

    private final ItemOrderRepository itemOrderRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public ItemOrderService(
            ItemOrderRepository itemOrderRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.itemOrderRepository = itemOrderRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<ItemOrder> getAllItems() {
        return itemOrderRepository.findAll();
    }

    public ItemOrder getItemById(Long id) {
        return itemOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item with id " + id + " not found."));
    }

    public List<ItemOrder> getItemsByOrderId(Long orderId) {
        getOrderById(orderId);
        return itemOrderRepository.findByOrderId(orderId);
    }

    public ItemOrder createItem(Long orderId, Long productId, @Valid ItemOrder payload) {
        Order order = getOrderById(orderId);
        Product product = getProductById(productId);

        ItemOrder itemOrder = new ItemOrder();
        itemOrder.setOrder(order);
        itemOrder.setProduct(product);
        itemOrder.setQuantity(validateQuantity(payload.getQuantity()));
        itemOrder.setUnitPrice(resolveUnitPrice(payload.getUnitPrice(), product));

        return itemOrderRepository.save(itemOrder);
    }

    public ItemOrder updateItem(Long id, Long productId, @Valid ItemOrder payload) {
        ItemOrder existingItem = getItemById(id);
        Product product = getProductById(productId);

        existingItem.setProduct(product);
        existingItem.setQuantity(validateQuantity(payload.getQuantity()));
        existingItem.setUnitPrice(resolveUnitPrice(payload.getUnitPrice(), product));

        return itemOrderRepository.save(existingItem);
    }

    public void deleteItem(Long id) {
        ItemOrder itemOrder = getItemById(id);
        itemOrderRepository.delete(itemOrder);
    }

    private int validateQuantity(Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new InvalidRequestException("Item quantity must be greater than zero.");
        }

        return quantity;
    }

    private BigDecimal resolveUnitPrice(BigDecimal unitPrice, Product product) {
        BigDecimal resolvedUnitPrice = unitPrice != null ? unitPrice : product.getPrice();

        if (resolvedUnitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Item unit price must be greater than zero.");
        }

        return resolvedUnitPrice;
    }

    private Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found."));
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found."));
    }
}
