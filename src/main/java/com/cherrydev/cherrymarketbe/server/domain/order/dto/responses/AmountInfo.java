package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AmountInfo(
        @NotNull Integer totalAmount,
        Integer discount,
        Integer couponAmount,
        Integer rewordAmount,
        Integer deliveryFee,
        String method
) {

        public static AmountInfo getInfo(PaymentDetailsInfo paymentDetailsInfo) {
                return new AmountInfo(
                        paymentDetailsInfo.totalAmount,
                        paymentDetailsInfo.discount,
                        paymentDetailsInfo.couponAmount,
                        paymentDetailsInfo.rewordAmount,
                        paymentDetailsInfo.deliveryFee,
                        paymentDetailsInfo.getPayment().getMethod()
                );
        }
}
