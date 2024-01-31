package com.cherrydev.cherrymarketbe.server.domain.order.dto.requests;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Order;
import com.cherrydev.cherrymarketbe.server.domain.payment.model.payment.Payment;
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
