package com.example.paymentservice.paymentGateway.Impl;

import com.example.paymentservice.paymentGateway.PaymentGateway;
import org.springframework.stereotype.Component;

@Component
public class StripePaymentGateway implements PaymentGateway {
    @Override
    public String generatePaymentLink(String orderId, Long amount) {
        //Call the Stripe Api to generate the Payment Link
        return "";
    }
}
