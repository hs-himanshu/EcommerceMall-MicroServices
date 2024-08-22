package com.example.paymentservice.paymentGateway.Impl;

import com.example.paymentservice.paymentGateway.PaymentGateway;
import com.razorpay.PaymentLink;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Primary
public class RazorPayPaymentGateway implements PaymentGateway {

    private final RazorpayClient razorpayClient;

    @Override
    public String generatePaymentLink(String orderId, Long amount) throws RazorpayException {
        //Call the RazorPay Api to generate the Payment Link

//        RazorpayClient razorpay = new RazorpayClient("[YOUR_KEY_ID]", "[YOUR_KEY_SECRET]");
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency","INR");
        paymentLinkRequest.put("accept_partial",true);
        paymentLinkRequest.put("first_min_partial_amount",100);
        paymentLinkRequest.put("expire_by",2039885364);
        paymentLinkRequest.put("reference_id",orderId.toString());
        paymentLinkRequest.put("description","Payment for order_id"+orderId.toString());
        JSONObject customer = new JSONObject();
        customer.put("name","+911234567895");
        customer.put("contact","Himanshu Soni");
        customer.put("email","hello@example.com");
        paymentLinkRequest.put("customer",customer);
        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);
        paymentLinkRequest.put("notify",notify);
        paymentLinkRequest.put("reminder_enable",true);
        JSONObject notes = new JSONObject();
        notes.put("policy_name","Jeevan Bima");
        paymentLinkRequest.put("notes",notes);
        paymentLinkRequest.put("callback_url","https://example-callback-url.com/");
        paymentLinkRequest.put("callback_method","get");

        PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);

        return payment.get("short_url").toString();
    }
}
