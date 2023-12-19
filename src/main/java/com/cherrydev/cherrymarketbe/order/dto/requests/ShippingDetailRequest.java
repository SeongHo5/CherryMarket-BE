package com.cherrydev.cherrymarketbe.order.dto.requests;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerAddress;
import com.cherrydev.cherrymarketbe.order.entity.ShippingDetails;

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
