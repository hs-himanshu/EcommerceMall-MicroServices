package com.example.ecommercemall.services;

import com.example.ecommercemall.exceptions.ProductNotFoundException;
import com.example.ecommercemall.models.Category;
import com.example.ecommercemall.models.Product;
import com.example.ecommercemall.repositories.CategoryRepository;
import com.example.ecommercemall.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    public Product getProductById(UUID id) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) return product.get();
        throw new ProductNotFoundException("Product not available!");
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}