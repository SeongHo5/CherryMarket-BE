package com.cherrydev.cherrymarketbe.order.dto;

import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChangeOrderStatus(
        @NotNull Long accoungId,
        @NotNull String orderCode,
        @NotNull String orderStatus
        ) {

    public Order changeOrderStatus() {

        return Order.builder()
                .accountId(this.accoungId)
                .orderCode(this.orderCode)
                .orderStatus(OrderStatus.valueOf(orderStatus))
                .build();
    }
}
