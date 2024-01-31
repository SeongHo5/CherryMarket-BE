package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.Order;
import com.cherrydev.cherrymarketbe.server.domain.order.enums.OrderStatus;
import com.cherrydev.cherrymarketbe.server.domain.payment.model.payment.Payment;

import java.util.List;

public record OrderReceiptResponse(
        Long accountId,
        String orderCode,
        OrderStatus orderStatus,
        List<GoodsInfo> goodsInfo,
        AmountInfo amountInfo,
        Payment payment
) {

    public static OrderReceiptResponse getOrder(Order order) {

        return new OrderReceiptResponse(
                order.getAccountId(),
                order.getOrderCode(),
                order.getOrderStatus(),
                order.getGoodsInfo(),
                order.getAmountInfo(),
                order.getPayment()
        );
    }

}
