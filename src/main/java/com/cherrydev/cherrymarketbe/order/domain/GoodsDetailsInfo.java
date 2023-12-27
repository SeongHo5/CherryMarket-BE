package com.cherrydev.cherrymarketbe.order.domain;

import com.cherrydev.cherrymarketbe.goods.dto.ToCartResponseDto;



public record GoodsDetailsInfo(
    Long goodsId,
    String storageType,
    String goodsName,
    String goodsCode,
    Integer quantity,
    int price,
    Integer inventory,
    Integer discountRate,
    Integer discountedPrice

) {
        public static GoodsDetailsInfo getGoodsDetails(GoodsInfo goodsInfo, ToCartResponseDto responseDto) {

            return new GoodsDetailsInfo(
                    responseDto.getGoodsId(),
                    responseDto.getStorageType(),
                    responseDto.getGoodsName(),
                    responseDto.getGoodsCode(),
                    goodsInfo.quantity(),
                    responseDto.getPrice(),
                    responseDto.getInventory(),
                    responseDto.getDiscountRate(),
                    responseDto.getDiscountedPrice()
            );

        }
}
