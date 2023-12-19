package com.cherrydev.cherrymarketbe.order.domain;

import com.cherrydev.cherrymarketbe.order.entity.PaymentDetails;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import lombok.Builder;

@Builder
public record PaymentSummary() {

    public PaymentDetails update(String orderCode, Payment cancelPayment, PaymentDetailsInfo paymentDetailsInfo) {

        return PaymentDetails.builder()
                .accountId(paymentDetailsInfo.getAccountId())
                .orderId(paymentDetailsInfo.getOrderId())
                .orderCode(orderCode)
                .payment(cancelPayment)
                .amountInfo(amountInfoUpdate(paymentDetailsInfo))
                .build();

    }

    public AmountInfo amountInfoUpdate(PaymentDetailsInfo paymentDetailsInfo) {
        return AmountInfo.builder()
                .totalAmount(paymentDetailsInfo.totalAmount)
                .rewordAmount(paymentDetailsInfo.rewordAmount)
                .couponAmount(paymentDetailsInfo.couponAmount)
                .deliveryFee(paymentDetailsInfo.deliveryFee)
                .discount(paymentDetailsInfo.discount)
                .build();
    }
}
