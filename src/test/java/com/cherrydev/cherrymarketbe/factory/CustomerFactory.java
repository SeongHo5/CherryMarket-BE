package com.cherrydev.cherrymarketbe.factory;

import com.cherrydev.cherrymarketbe.customer.dto.address.AddAddressRequestDto;

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
}
