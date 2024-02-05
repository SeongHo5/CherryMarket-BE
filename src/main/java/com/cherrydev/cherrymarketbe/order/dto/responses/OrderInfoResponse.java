package com.cherrydev.cherrymarketbe.order.dto.responses;

import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record OrderInfoResponse(
        @NotNull String orderCode,
        @NotNull String orderStatus,
        @NotNull Integer amount,
        @NotNull String orderName,
        @NotNull String paymentMethod,
        @NotNull String goodsCode,
        @JsonFormat(pattern = "yyyy.MM.dd (HH시 mm분)")
        @NotNull Timestamp createdAt

) {

    public static OrderInfoResponse getOrder(Order order) {

        return new OrderInfoResponse(
                order.getOrderCode(),
                order.getOrderStatus().toString(),
                (int) order.getPayment().getCard().getAmount(),
                order.getOrderName(),
                order.getPayment().getMethod(),
                order.getRepresentativeGoodsCode(),
                order.getCreatedAt()
        );
    }



}
