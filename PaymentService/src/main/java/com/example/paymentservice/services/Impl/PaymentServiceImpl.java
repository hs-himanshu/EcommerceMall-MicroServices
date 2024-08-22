package com.example.paymentservice.services.Impl;

import com.example.paymentservice.paymentGateway.PaymentGateway;
import com.example.paymentservice.services.PaymentService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentGateway paymentGateway;

    @Override
    public String initiatePayment(String orderId, Long amount, String paymentMethod) throws RazorpayException {
        //Make a call to payment gateway to generate the link
        System.out.println("Amount-"+amount);
       return paymentGateway.generatePaymentLink(orderId,amount);
    }
}
