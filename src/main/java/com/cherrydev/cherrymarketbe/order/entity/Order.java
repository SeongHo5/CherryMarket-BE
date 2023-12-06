package com.cherrydev.cherrymarketbe.order.entity;


import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class Order {

    private Long orderId;

    private Long accountId;

    private String orderCode;

    private OrderStatus orderStatus;


    @Builder
    public Order(Long orderId, Long accountId, String orderCode, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.accountId = accountId;
        this.orderCode = orderCode;
        this.orderStatus = orderStatus;
    }

    public static String createOrderCode(){
        return UUID.randomUUID().toString().substring(16).replace("-","").toUpperCase();
    }

    public boolean isDeleted() {
        return this.orderStatus == OrderStatus.DELETED;
    }

}
