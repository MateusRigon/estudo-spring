package com.projeto.estudo.repository;

import com.projeto.estudo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientId(int clientId);
}
