package com.cherrydev.cherrymarketbe.goods.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GoodsBasicInfoResponseDto {

    Long goodsId;

    String goodsName;

    String goodsCode;

    String description;

    int price;

    Integer discountRate;

    Integer discountedPrice;
}
