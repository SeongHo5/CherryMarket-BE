package com.cherrydev.cherrymarketbe.order.domain;

import com.cherrydev.cherrymarketbe.goods.dto.ToCartResponseDto;
import com.cherrydev.cherrymarketbe.order.entity.ProductDetails;


public record GoodsDetailsInfo(
    Long goodsId,
    String storageType,
    String goodsName,
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
                    goodsInfo.quantity(),
                    responseDto.getPrice(),
                    responseDto.getInventory(),
                    responseDto.getDiscountRate(),
                    responseDto.getDiscountedPrice()
            );

        }
}
