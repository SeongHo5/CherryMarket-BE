package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddressInfo {

    Long addressId;
    boolean isDefault;
    String name;
    String zipcode;
    String address;
    String addressDetail;
    String createdAt;
}
