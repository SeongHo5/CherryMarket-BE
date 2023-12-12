package com.cherrydev.cherrymarketbe.customer.dto.address;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddressInfoDto {

    Long addressId;
    boolean isDefault;
    String name;
    String zipcode;
    String address;
    String addressDetail;
    String createdAt;
}
