package com.example.ecommercemall.services;

import com.example.ecommercemall.models.Order;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendOrderConfirmation(Long userId,Order order) {
        // Implement email sending logic here
        // Send an email to the user with the order details
        System.out.println("Sending order confirmation email for Order ID: " + order.getId());
        // Example: emailSender.send(...);
    }
}
