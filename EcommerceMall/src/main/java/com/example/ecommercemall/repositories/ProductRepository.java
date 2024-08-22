package com.example.ecommercemall.repositories;

import com.example.ecommercemall.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<List<Product>> findAllByUserId(String userId);
    Optional<List<Product>> findAllById(UUID id);
}