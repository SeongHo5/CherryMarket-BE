package com.cherrydev.cherrymarketbe.payments.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Value
public class PaymentApproveForm implements Serializable {

    @NotNull
    String paymentKey;

    @NotNull
    String orderId;

    @NotNull
    Number amount;

    public PaymentApproveForm(String paymentKey, String orderId, Number amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }

    public Map<String, Object> toRequestBody() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", this.getOrderId());
        requestBody.put("paymentKey", this.getPaymentKey());
        requestBody.put("amount", this.getAmount());
        return requestBody;
    }
}
