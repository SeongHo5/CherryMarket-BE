package com.cherrydev.cherrymarketbe.factory;

import com.cherrydev.cherrymarketbe.customer.dto.address.AddAddressRequestDto;
import com.cherrydev.cherrymarketbe.customer.dto.address.ModifyAddressRequestDto;

public class CustomerFactory {

    public static AddAddressRequestDto createAddAddressRequestDtoA() {
        return new AddAddressRequestDto(
                false,
                "name",
                "51234",
                "address 1",
                "addressDetail 1"
        );
    }

    public static AddAddressRequestDto createAddAddressRequestDtoB() {
        return new AddAddressRequestDto(
                true,
                "name",
                "51234",
                "address 1",
                "addressDetail 1"
        );
    }

    public static ModifyAddressRequestDto createModifyAddressRequestDtoA() {
        return new ModifyAddressRequestDto(
                1L,
                false,
                "name",
                "51234",
                "address 1",
                "addressDetail 1"
        );
    }
}
