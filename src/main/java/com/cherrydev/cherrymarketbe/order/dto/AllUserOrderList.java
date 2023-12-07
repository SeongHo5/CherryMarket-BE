package com.cherrydev.cherrymarketbe.order.dto;

import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record AllUserOrderList(
        Map<OrderStatus, List<OrderDetails>> allOrders
) {
    public AllUserOrderList(List<Order> orders) {
        this(
                orders.stream()
                        .map(OrderDetails::getOrdersList)
                        .collect(Collectors.groupingBy(OrderDetails::orderStatus))
        );
    }
}
