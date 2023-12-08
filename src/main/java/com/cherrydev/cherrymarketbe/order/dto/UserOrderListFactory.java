package com.cherrydev.cherrymarketbe.order.dto;

import com.cherrydev.cherrymarketbe.order.entity.Order;

import java.util.List;

public class UserOrderListFactory {

    public static UserOrderList find(List<Order> orders) {

        return new UserOrderList(
                orders.stream()
                        .map(OrderDetails::getOrder)
                        .toList()
        );
    }
}




