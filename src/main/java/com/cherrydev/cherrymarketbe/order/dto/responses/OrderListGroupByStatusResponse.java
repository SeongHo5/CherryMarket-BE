package com.cherrydev.cherrymarketbe.order.dto.responses;

import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record OrderListGroupByStatusResponse(
        Map<OrderStatus, List<OrderInfoResponse>> allOrders
) {
    public OrderListGroupByStatusResponse(List<Order> orders) {
        this(
                orders.stream()
                        .map(order ->
                                new AbstractMap.SimpleEntry<>(order.getOrderStatus(), OrderInfoResponse.getOrder(order)))
                        .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())))
        );
    }
}
