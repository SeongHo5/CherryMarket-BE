package com.cherrydev.cherrymarketbe.goods.dto;

import lombok.Value;

@Value
public class GoodsBasicInfoDto {

    Long goodsId;

    String goodsName;

    String goodsCode;

    String description;

    int price;

    Integer discountRate;
}
