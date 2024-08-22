package com.example.paymentservice.controllers;

import com.example.paymentservice.dtos.PaymentRequestDTO;
import com.example.paymentservice.dtos.PaymentResponseDTO;
import com.example.paymentservice.services.PaymentService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    //payment microservice should call order service to confirm order id
    //should implement spring security send tokens
    private final PaymentService paymentService;

    @PostMapping("/razorpay")
    public PaymentResponseDTO processPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) throws RazorpayException {
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
        //removing last two decimal from amount ex 54.34 -> 5432
        Long amount = paymentRequestDTO.getAmount();
        String response = paymentService.initiatePayment(paymentRequestDTO.getOrderId(),
                amount,paymentRequestDTO.getPaymentProvider());
        paymentResponseDTO.setPaymentLink(response);
        paymentResponseDTO.setPaymentStatus("SUCCESS");
        return paymentResponseDTO;
    }
}
