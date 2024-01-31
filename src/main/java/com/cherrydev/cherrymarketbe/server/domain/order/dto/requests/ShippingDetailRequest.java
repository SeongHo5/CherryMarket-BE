package com.cherrydev.cherrymarketbe.server.domain.order.dto.requests;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerAddress;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.ShippingDetails;

import lombok.Builder;

@Builder
public record ShippingDetailRequest() {

    public ShippingDetails create(AccountDetails accountDetails, Long orderId, String orderCode, CustomerAddress customerAddress) {

        return ShippingDetails.builder()
                .account(accountDetails.getAccount())
                .orderId(orderId)
                .orderCode(orderCode)
                .customerAddress(customerAddress)
                .build();
    }

}
