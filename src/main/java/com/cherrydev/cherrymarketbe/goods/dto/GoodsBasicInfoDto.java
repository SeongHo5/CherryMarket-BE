package com.cherrydev.cherrymarketbe.goods.dto;

import lombok.Value;

@Value
public class GoodsBasicInfoDto {

    Long goodsId;

    String goodsName;

    int price;

    Integer discountRate;
}
