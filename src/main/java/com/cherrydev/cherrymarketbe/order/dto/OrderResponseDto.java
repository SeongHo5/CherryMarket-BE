package com.cherrydev.cherrymarketbe.order.dto;


import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;


public record OrderResponseDto(
        Long orderId,
        String orderCode,
        OrderStatus orderStatus
) {

    public static OrderResponseDto getOrdersList(Order order) {

        return new OrderResponseDto(
                order.getOrderId(),
                order.getOrderCode(),
                order.getOrderStatus()
        );
    }

}
