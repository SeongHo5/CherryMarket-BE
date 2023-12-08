package com.cherrydev.cherrymarketbe.customer.dto.address;

import jakarta.validation.constraints.Pattern;
import lombok.Value;

@Value
public class ModifyAddressRequestDto {

    Long addressId;

    Boolean isDefault;

    String name;

    @Pattern(regexp = "^[0-9]{5}$", message = "우편번호는 5자리 숫자입니다.")
    String zipcode;

    String address;

    String addressDetail;
}

