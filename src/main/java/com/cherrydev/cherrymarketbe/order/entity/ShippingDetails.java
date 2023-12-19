package com.cherrydev.cherrymarketbe.order.entity;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerAddress;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetails {

    @NotNull
    private Long orderId;

    private Account account;

    @NotNull
    private String orderCode;

    @NotNull
    private CustomerAddress customerAddress;

    private String deliveryStatus;

}
