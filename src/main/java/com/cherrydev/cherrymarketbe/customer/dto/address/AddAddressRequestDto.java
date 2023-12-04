package com.cherrydev.cherrymarketbe.customer.dto.address;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerAddress;
import jakarta.validation.constraints.Pattern;

public record AddAddressRequestDto(
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
