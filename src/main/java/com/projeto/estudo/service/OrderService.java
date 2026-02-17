package com.projeto.estudo.service;

import com.projeto.estudo.handler.InvalidRequestException;
import com.projeto.estudo.handler.ResourceNotFoundException;
import com.projeto.estudo.messaging.OrderCreatedEvent;
import com.projeto.estudo.messaging.OrderCreatedPublisher;
import com.projeto.estudo.model.Client;
import com.projeto.estudo.model.ItemOrder;
import com.projeto.estudo.model.Order;
import com.projeto.estudo.model.Product;
import com.projeto.estudo.repository.ClientRepository;
import com.projeto.estudo.repository.OrderRepository;
import com.projeto.estudo.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderCreatedPublisher orderCreatedPublisher;

    public OrderService(
            OrderRepository orderRepository,
            ClientRepository clientRepository,
            ProductRepository productRepository,
            OrderCreatedPublisher orderCreatedPublisher
    ) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.orderCreatedPublisher = orderCreatedPublisher;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByClientId(int clientId) {
        getClientById(clientId);
        return orderRepository.findByClientId(clientId);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found."));
    }

    @Transactional
    public Order createOrder(int clientId, @Valid Order payload) {
        Client client = getClientById(clientId);

        Order order = new Order();
        order.setClient(client);
        replaceOrderItems(order, payload.getItems());

        Order savedOrder = orderRepository.save(order);
        orderCreatedPublisher.publish(buildOrderCreatedEvent(savedOrder));
        return savedOrder;
    }

    @Transactional
    public Order updateOrder(Long id, @Valid Order payload) {
        Order existingOrder = getOrderById(id);

        if (payload.getClient() != null && payload.getClient().getId() != 0) {
            Client newClient = getClientById(payload.getClient().getId());
            existingOrder.setClient(newClient);
        }

        if (payload.getItems() != null) {
            replaceOrderItems(existingOrder, payload.getItems());
        }

        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }

    private void replaceOrderItems(Order order, List<ItemOrder> itemPayloads) {
        if (itemPayloads == null || itemPayloads.isEmpty()) {
            throw new InvalidRequestException("An order must contain at least one item.");
        }

        List<ItemOrder> existingItems = new ArrayList<>(order.getItems());
        for (ItemOrder existingItem : existingItems) {
            order.removeItem(existingItem);
        }

        for (ItemOrder itemPayload : itemPayloads) {
            order.addItem(createOrderItem(itemPayload));
        }
    }

    private ItemOrder createOrderItem(ItemOrder itemPayload) {
        if (itemPayload == null) {
            throw new InvalidRequestException("Item payload cannot be null.");
        }

        if (itemPayload.getProduct() == null || itemPayload.getProduct().getId() == null) {
            throw new InvalidRequestException("Each order item must reference a product id.");
        }

        Product product = productRepository.findById(itemPayload.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + itemPayload.getProduct().getId() + " not found."
                ));

        Integer quantity = itemPayload.getQuantity();
        if (quantity == null || quantity < 1) {
            throw new InvalidRequestException("Item quantity must be greater than zero.");
        }

        BigDecimal unitPrice = itemPayload.getUnitPrice() != null ? itemPayload.getUnitPrice() : product.getPrice();
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Item unit price must be greater than zero.");
        }

        ItemOrder item = new ItemOrder();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        return item;
    }

    private Client getClientById(int id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found."));
    }

    private OrderCreatedEvent buildOrderCreatedEvent(Order order) {
        List<OrderCreatedEvent.OrderItemEvent> items = order.getItems()
                .stream()
                .map(item -> new OrderCreatedEvent.OrderItemEvent(
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ))
                .collect(Collectors.toList());

        return new OrderCreatedEvent(
                order.getId(),
                order.getClient().getId(),
                order.getCreatedAt(),
                items
        );
    }
}
