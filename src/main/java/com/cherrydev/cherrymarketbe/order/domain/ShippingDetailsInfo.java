package com.cherrydev.cherrymarketbe.order.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetailsInfo {

    @NotNull String orderCode;
    @NotNull String shippingStatus;
    @NotNull String recipient;
    @NotNull String recipientContact;
    @NotNull String zipCode;
    @NotNull String address;
    @NotNull String addressDetail;
    String place;
    String request;

}
