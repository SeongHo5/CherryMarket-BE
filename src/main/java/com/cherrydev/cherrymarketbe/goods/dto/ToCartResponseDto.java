package com.cherrydev.cherrymarketbe.goods.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ToCartResponseDto {

    Long goodsId;

    String goodsName;

    String goodsCode;

    int price;

    int inventory;

    String storageType;

    String salesStatus;

    Integer discountRate;

    Integer discountedPrice;

    @Builder
    public ToCartResponseDto(Long goodsId, String goodsName, String goodsCode, int price, int inventory, String storageType, String salesStatus, Integer discountRate, Integer discountedPrice) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsCode = goodsCode;
        this.price = price;
        this.inventory = inventory;
        this.storageType = storageType;
        this.salesStatus = salesStatus;
        this.discountRate = discountRate;
        this.discountedPrice = discountedPrice;
    }

    public Integer getDiscountedPrice() {
        return discountRate != null ? price - (price * discountRate / 100) : null;
    }
}