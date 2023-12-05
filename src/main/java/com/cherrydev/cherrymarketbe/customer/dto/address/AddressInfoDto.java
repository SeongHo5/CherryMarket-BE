package com.cherrydev.cherrymarketbe.customer.dto.address;

import lombok.Value;

@Value
public class AddressInfoDto {

    boolean isDefault;
    String name;
    String zipcode;
    String address;
    String addressDetail;
    String createdAt;
}
