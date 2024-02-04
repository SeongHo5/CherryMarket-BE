package com.cherrydev.cherrymarketbe.server.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;

public record RequestCreateOrder(
        @NotNull
        String orderName,
        String recipient,
        String recipientContact,
        String zipCode,
        String address,
        String addressDetail,
        String place,
        String request
) {
}
