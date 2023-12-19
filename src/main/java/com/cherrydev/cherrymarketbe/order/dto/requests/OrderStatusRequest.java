package com.cherrydev.cherrymarketbe.order.dto.requests;

import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus;
import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderStatusRequest(
        @NotNull String orderCode,
        @NotNull String orderStatus
        ) {

    public Order changeOrderStatus() {

        validateFields();

        return Order.builder()
                .orderCode(this.orderCode)
                .orderStatus(OrderStatus.valueOf(orderStatus))
                .build();
    }

    private void validateFields() {
        if (this.orderCode == null) {
            throw new NotFoundException(ExceptionStatus.INVALID_INPUT_VALUE);
        }
        if (this.orderStatus == null) {
            throw new NotFoundException(ExceptionStatus.INVALID_INPUT_VALUE);
        }

        try {
            OrderStatus.valueOf(orderStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(ExceptionStatus.INVALID_INPUT_VALUE);
        }
    }
}
