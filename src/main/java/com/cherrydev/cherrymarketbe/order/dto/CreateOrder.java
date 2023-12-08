package com.cherrydev.cherrymarketbe.order.dto;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import lombok.Builder;

@Builder
public record CreateOrder() {

    public Order create(Account account) {

        return Order.builder()
                .accountId(account.getAccountId())
                .orderCode(Order.createOrderCode())
                .orderStatus(OrderStatus.PROCESSING)
                .build();
    }

}
