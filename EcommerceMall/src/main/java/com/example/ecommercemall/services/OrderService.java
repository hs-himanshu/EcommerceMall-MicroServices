package com.example.ecommercemall.services;

import com.example.ecommercemall.dtos.OrderRequestDTO;
import com.example.ecommercemall.exceptions.InsufficientStockException;
import com.example.ecommercemall.exceptions.ProductNotFoundException;
import com.example.ecommercemall.exceptions.ResourceNotFoundException;
import com.example.ecommercemall.models.Order;
import com.example.ecommercemall.models.OrderItem;
import com.example.ecommercemall.models.OrderStatus;
import com.example.ecommercemall.models.Product;
import com.example.ecommercemall.repositories.OrderItemRepository;
import com.example.ecommercemall.repositories.OrderRepository;
import com.example.ecommercemall.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Order placeOrder(OrderRequestDTO orderRequestDTO) throws InsufficientStockException, ProductNotFoundException {

        Order order = new Order();
        order.setOrderDate(Instant.now());
        order.setTotal(orderRequestDTO.getTotal());
        order.setUserId(orderRequestDTO.getUserId());
        order.setOrderItems(orderRequestDTO.getOrderItems());
        order.setOrderStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItem item : orderRequestDTO.getOrderItems()) {
            // Check if the product exists and has enough stock
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + item.getProductId()));

            if (product.getStock() < item.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for product: " + product.getName());
            }

            // Deduct the stock from the product
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);  // Save the updated product stock

            // Add item to order
            item.setOrder(order);
            orderItems.add(item);
        }
        // Set the orderItems to the order
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        emailService.sendOrderConfirmation(order.getUserId(),savedOrder);

        return savedOrder;

    }

    public List<Order> getAllOrdersByUserId(Long userId) {
        // Fetch orders by userId from the repository
        List<Order> orders = orderRepository.findByUserId(userId);

        // For each order, iterate over order items
        for (Order order : orders) {
            List<OrderItem> orderItems = order.getOrderItems();

            // For each order item, fetch the associated product using productId
            for (OrderItem orderItem : orderItems) {
                UUID productId = orderItem.getProductId();

                // Fetch the product using the productId
                List<Product> product = productRepository.findAllById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

                // Manually set the product details to the order item
                orderItem.setProduct(product);  // If needed, a transient field in OrderItem can be used
            }
        }

        // Return the populated orders list with associated order items and products
        return orders;
    }
}
