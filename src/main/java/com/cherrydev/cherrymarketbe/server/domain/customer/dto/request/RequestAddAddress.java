package com.cherrydev.cherrymarketbe.server.domain.customer.dto.request;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerAddress;

public record RequestAddAddress(
        Boolean isDefault,
        String name,
        String zipcode,
        String address,
        String addressDetail) {

    public CustomerAddress toEntity(Account account) {
        return CustomerAddress.builder()
                .accountId(account.getAccountId())
                .isDefault(isDefault)
                .name(name)
                .zipCode(zipcode)
                .address(address)
                .addressDetail(addressDetail)
                .build();
    }
}
