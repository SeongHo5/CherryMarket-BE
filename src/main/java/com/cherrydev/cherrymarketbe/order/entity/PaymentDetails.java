package com.cherrydev.cherrymarketbe.order.entity;

import com.cherrydev.cherrymarketbe.order.domain.AmountInfo;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {

    @NotNull private Long accountId;

    @NotNull private Long orderId;

    @NotNull private String orderCode;

    @NotNull private Payment payment;

    @NotNull private AmountInfo amountInfo;


}
