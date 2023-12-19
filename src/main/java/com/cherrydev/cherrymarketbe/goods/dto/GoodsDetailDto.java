package com.cherrydev.cherrymarketbe.goods.dto;

import lombok.Value;

@Value
public class GoodsDetailDto {

    Long goodsId;

    String goodsCode;

    String goodsName;

    String description;

    int price;

    int inventory;

    String storageType;

    String capacity;

    String expDate;

    String allergyInfo;

    String originPlace;

    String salesStatus;

    Integer discountRate;

    String makerName;

}
