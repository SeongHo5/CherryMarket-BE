package com.cherrydev.cherrymarketbe.goods.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GoodsInventoryResponseDto {

    Long goodsId;

    String salesStatus;

    int inventory;
}
