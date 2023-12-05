package com.cherrydev.cherrymarketbe.auth.dto.oauth.kakao;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerAddress;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class KakaoAddressInfoDto {

    @JsonProperty("user_id")
    Long id;

    @JsonProperty("shipping_addresses")
    List<KakaoAddress> kakaoAddress;

    @Value
    public static class KakaoAddress {

        @JsonProperty("default")
        Boolean isDefault;

        @JsonProperty("name")
        String name;

        @JsonProperty("zone_number")
        String zipcode;

        @JsonProperty("base_address")
        String address;

        @JsonProperty("detail_address")
        String addressDetail;

    }

    public CustomerAddress toEntity(Account account, KakaoAddress kakaoAddress) {
        return CustomerAddress.builder()
                .accountId(account.getAccountId())
                .isDefault(kakaoAddress.getIsDefault())
                .name(kakaoAddress.getName())
                .zipCode(kakaoAddress.getZipcode())
                .address(kakaoAddress.getAddress())
                .addressDetail(kakaoAddress.getAddressDetail())
                .build();
    }

}
