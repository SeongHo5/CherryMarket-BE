package com.cherrydev.cherrymarketbe.server.domain.order.dto.requests;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.PaymentDetails;
import com.cherrydev.cherrymarketbe.server.domain.payment.model.payment.Payment;

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
