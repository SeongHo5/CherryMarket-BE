package com.cherrydev.cherrymarketbe.goods.dto;

import lombok.Value;

@Value
public class GoodsListDto {

    Long goodsId;
    String goodsName;
    int price;
}
