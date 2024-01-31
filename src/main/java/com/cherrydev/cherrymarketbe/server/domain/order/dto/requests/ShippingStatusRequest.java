package com.cherrydev.cherrymarketbe.server.domain.order.dto.requests;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.ShippingDetails;
import com.cherrydev.cherrymarketbe.server.domain.order.enums.OrderStatus;
import lombok.Builder;

@Builder
public record ShippingStatusRequest(
        String orderCode,
        String deliveryStatus
        ) {

    public ShippingDetails changeDeliveryStatus() {

        validateFields();

        return ShippingDetails.builder()
                .orderCode(orderCode)
                .deliveryStatus(deliveryStatus)
                .build();
    }

    private void validateFields() {
        if (this.orderCode == null) {
            throw new NotFoundException(ExceptionStatus.INVALID_INPUT_VALUE);
        }
        if (this.deliveryStatus == null) {
            throw new NotFoundException(ExceptionStatus.INVALID_INPUT_VALUE);
        }

        try {
            OrderStatus.valueOf(deliveryStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(ExceptionStatus.INVALID_INPUT_VALUE);
        }
    }
}
