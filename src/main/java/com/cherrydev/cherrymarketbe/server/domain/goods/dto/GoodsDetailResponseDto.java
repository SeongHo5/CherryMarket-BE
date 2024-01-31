package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GoodsDetailResponseDto {

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

    Integer discountedPrice;

    String makerName;

    List<String> goodsImageUrls;
}
