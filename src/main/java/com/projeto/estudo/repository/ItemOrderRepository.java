package com.projeto.estudo.repository;

import com.projeto.estudo.model.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemOrderRepository extends JpaRepository<ItemOrder, Long> {
    List<ItemOrder> findByOrderId(Long orderId);
}
