package com.cherrydev.cherrymarketbe.order.dto.requests;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.order.entity.PaymentDetails;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;

public record PaymentDetailRequest() {

    public PaymentDetails create(AccountDetails accountDetails, OrderReceiptResponse orderAllDetailsRequest, Payment orderWithPayment, Long orderId) {

        return PaymentDetails.builder()
                .accountId(accountDetails.getAccount().getAccountId())
                .orderId(orderId)
                .orderCode(orderAllDetailsRequest.orderCode())
                .payment(orderWithPayment)
                .amountInfo(orderAllDetailsRequest.amountInfo())
                .build();
    }
}
