package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.Order;

import java.util.List;

public class OrderSummaryListFactory {

    public static OrderSummaryList find(List<Order> orders) {

        return new OrderSummaryList(
                orders.stream()
                        .map(OrderInfoResponse::getOrder)
                        .toList()
        );
    }
}




