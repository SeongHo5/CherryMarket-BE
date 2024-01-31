package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

import com.cherrydev.cherrymarketbe.server.domain.discount.entity.Discount;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import lombok.Builder;

@Builder
public record GoodsInfo(
        Long goodsId,
        String goodsName,
        String goodsCode,
        String description,
        Integer price,
        Integer discountRate,
        Integer discountedPrice
) {

    public static GoodsInfo of(Goods goods) {
        return GoodsInfo.builder()
                .goodsId(goods.getId())
                .goodsName(goods.getName())
                .goodsCode(goods.getCode())
                .description(goods.getDescription())
                .price(goods.getPrice())
                .discountRate(goods.getDiscountRate())
                .discountedPrice((int) (goods.getPrice() * (1 - (goods.getDiscountRate() / 100.0))))
                .build();
    }

}
