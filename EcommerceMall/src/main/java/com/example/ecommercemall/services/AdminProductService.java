package com.example.ecommercemall.services;

import com.example.ecommercemall.dtos.ReceivedOrderResponseDTO;
import com.example.ecommercemall.exceptions.ResourceNotFoundException;
import com.example.ecommercemall.models.OrderItem;
import com.example.ecommercemall.models.Product;
import com.example.ecommercemall.repositories.OrderItemRepository;
import com.example.ecommercemall.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminProductService {
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public List<Product> getAllProducts(String userId) {

        Optional<List<Product>> products =  productRepository.findAllByUserId(userId);
        if(products.isPresent()){
            return products.get();
        }
        throw new ResourceNotFoundException("No products found");
    }

    public void createProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public List<ReceivedOrderResponseDTO> getAllReceivedOrders(String userId) {
        //find all product ids in orderItems
        //check these product has this userId : Product Table
        Optional<List<Product>> products =  productRepository.findAllByUserId(userId); //my products
        List<Product> myProductList ; // my products
        if(products.isEmpty()){
            throw new ResourceNotFoundException("No products found");
        }
        else myProductList = products.get();
        List<ReceivedOrderResponseDTO> response = new ArrayList<>();
        for(Product product: myProductList){
            Optional<OrderItem> orderItem = orderItemRepository.findByProductId(product.getId());
            if(orderItem.isPresent()){
                OrderItem singleOrderItem = orderItem.get();
                ReceivedOrderResponseDTO responseDTO = new ReceivedOrderResponseDTO();
                responseDTO.setOrderId(String.valueOf(singleOrderItem.getOrder().getId()));
                responseDTO.setProductId(String.valueOf(singleOrderItem.getProductId()));
                responseDTO.setProductName(product.getName());
                responseDTO.setProductDescription(product.getDescription());
                responseDTO.setQuantity(singleOrderItem.getQuantity());
                responseDTO.setPrice(product.getPrice());
                responseDTO.setImgUrl(product.getImageUrl());
                response.add(responseDTO);
            }
        }
        return response;
    }
}
