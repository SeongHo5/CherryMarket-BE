package com.cherrydev.cherrymarketbe.goods.dto;

import lombok.Value;

@Value
public class ToCartDto {

    Long goodsId;

    String goodsName;

    int price;

    int inventory;

    String storageType;

    String salesStatus;

    Integer discountRate;
}
