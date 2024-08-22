package com.example.ecommercemall.repositories;

import com.example.ecommercemall.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findAllByOrderId(UUID aLong);
    Optional<OrderItem> findByProductId(UUID id);
}