package com.cherrydev.cherrymarketbe.order.dto;

import com.cherrydev.cherrymarketbe.order.entity.Order;

import java.util.List;

public class UserOrderListFactory {

    public static UserOrderList create(List<Order> orders) {

        return new UserOrderList(
                orders.stream()
                        .map(OrderDetails::getOrdersList)
                        .toList()
        );
    }
}




