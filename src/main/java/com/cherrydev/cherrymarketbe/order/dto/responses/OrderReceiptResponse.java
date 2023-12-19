package com.cherrydev.cherrymarketbe.order.dto.responses;

import com.cherrydev.cherrymarketbe.order.domain.AmountInfo;
import com.cherrydev.cherrymarketbe.order.domain.GoodsInfo;
import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;

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
