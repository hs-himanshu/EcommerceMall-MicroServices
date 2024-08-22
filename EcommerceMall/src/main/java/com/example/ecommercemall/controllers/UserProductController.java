package com.example.ecommercemall.controllers;

import com.example.ecommercemall.dtos.*;
import com.example.ecommercemall.exceptions.InsufficientStockException;
import com.example.ecommercemall.exceptions.ProductNotFoundException;
import com.example.ecommercemall.models.Category;
import com.example.ecommercemall.models.Order;
import com.example.ecommercemall.models.OrderItem;
import com.example.ecommercemall.models.Product;
import com.example.ecommercemall.services.OrderService;
import com.example.ecommercemall.services.UserProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserProductController {

    private final UserProductService userProductService;
    private final OrderService orderService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World from User Service";
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> products = userProductService.getAllProducts(page, size);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) throws ProductNotFoundException {
        Product product = userProductService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = userProductService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/place-order")
    public ResponseEntity<ResponseDTO> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) throws InsufficientStockException, ProductNotFoundException {
        Order order = orderService.placeOrder(orderRequestDTO);
        ResponseDTO response = new ResponseDTO("Order received. Check your email for details.", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-orders")
    public OrderResponseDTO getMyOrders(@RequestBody UserIdDTO userIdDTO) {
        Long userId = userIdDTO.getUserId();

        List<Order> orders = orderService.getAllOrdersByUserId(userId);
        //orders -> orderItem -> product -> category
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();

        for(Order order : orders) {
            orderResponseDTO.setOrderId(order.getId());
            orderResponseDTO.setOrderDate(order.getOrderDate());
            orderResponseDTO.setTotal(order.getTotal());
            orderResponseDTO.setOrderStatus(order.getOrderStatus());
            List<OrderItemResponseDTO> orderItems = new ArrayList<>();
            for(OrderItem orderItem : order.getOrderItems()) {
                OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();
                orderItemResponseDTO.setOrderItemId(orderItem.getId());
                orderItemResponseDTO.setQuantity(orderItem.getQuantity());
                List<ProductResponseDTO> products = new ArrayList<>();
                for(Product product: orderItem.getProduct()){
                    ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                    productResponseDTO.setProductId(product.getId());
                    productResponseDTO.setName(product.getName());
                    productResponseDTO.setDescription(product.getDescription());
                    productResponseDTO.setPrice(product.getPrice());
                    productResponseDTO.setImageUrl(product.getImageUrl());
                    products.add(productResponseDTO);
                }
                orderItemResponseDTO.setProducts(products);
                orderItems.add(orderItemResponseDTO);
            }
            orderResponseDTO.setOrderItems(orderItems);

        }


        return orderResponseDTO;
    }

}
