package com.cherrydev.cherrymarketbe.order.dto.requests;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import lombok.Builder;

@Builder
public record OrderDetailRequest() {

    public Order create(AccountDetails accountDetails, OrderReceiptResponse orderAllDetailsRequest, Payment orderWithPayment) {

        return Order.builder()
                .accountId(accountDetails.getAccount().getAccountId())
                .orderCode(orderAllDetailsRequest.orderCode())
                .orderStatus(orderAllDetailsRequest.orderStatus())
                .orderName(orderWithPayment.getOrderName())
                .representativeGoodsId(orderAllDetailsRequest.goodsInfo().get(0).goodsId())
                .build();
    }

}
