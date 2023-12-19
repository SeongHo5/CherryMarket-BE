package com.cherrydev.cherrymarketbe.order.dto.responses;

import com.cherrydev.cherrymarketbe.order.entity.Order;

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




