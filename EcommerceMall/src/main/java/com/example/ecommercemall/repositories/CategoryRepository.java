package com.example.ecommercemall.repositories;

import com.example.ecommercemall.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
