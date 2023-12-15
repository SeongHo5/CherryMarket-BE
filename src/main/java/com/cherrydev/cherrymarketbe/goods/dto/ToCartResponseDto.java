package com.cherrydev.cherrymarketbe.goods.dto;


import lombok.*;


@Getter
@NoArgsConstructor
public class ToCartResponseDto {

    Long goodsId;

    String goodsName;

    int price;

    int inventory;

    String storageType;

    String salesStatus;

    Integer discountRate;

    Integer discountedPrice;

    @Builder
    public ToCartResponseDto(Long goodsId, String goodsName, int price, int inventory, String storageType, String salesStatus, Integer discountRate, Integer discountedPrice) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
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