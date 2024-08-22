package com.example.paymentservice.services;

import com.razorpay.RazorpayException;
import org.springframework.stereotype.Service;


public interface PaymentService {
    public String initiatePayment(String orderId, Long amount , String paymentMethod) throws RazorpayException;
}
