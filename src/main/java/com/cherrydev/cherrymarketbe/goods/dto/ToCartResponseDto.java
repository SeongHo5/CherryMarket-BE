package com.cherrydev.cherrymarketbe.goods.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ToCartResponseDto {

    Long goodsId;

    String goodsName;

    int price;

    int inventory;

    String storageType;

    String salesStatus;

    Integer discountRate;

    Integer discountedPrice;
}
