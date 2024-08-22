package com.example.paymentservice.paymentGateway;

import com.razorpay.RazorpayException;

public interface PaymentGateway {
    String generatePaymentLink(String orderId, Long amount) throws RazorpayException;
}
