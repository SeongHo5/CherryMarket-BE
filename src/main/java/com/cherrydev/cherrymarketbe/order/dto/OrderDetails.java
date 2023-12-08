package com.cherrydev.cherrymarketbe.order.dto;

import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;

public record OrderDetails(
        Long orderId,
        String orderCode,
        OrderStatus orderStatus
) {

    public static OrderDetails getOrder(Order order) {

        return new OrderDetails(
                order.getOrderId(),
                order.getOrderCode(),
                order.getOrderStatus()
        );
    }

}
