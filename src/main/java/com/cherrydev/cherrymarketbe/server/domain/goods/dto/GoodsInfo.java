package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Discount;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import lombok.Builder;

@Builder
public record GoodsInfo(
        String goodsName,
        String goodsCode,
        String description,
        Integer price,
        Integer discountRate,
        Integer discountedPrice
) {

    public static GoodsInfo of(Goods goods, Discount discount) {
        int discountRate = discount.getDiscountRate();
        int discountedPrice = (int) (goods.getPrice() * (1 - (discountRate / 100.0)));
        return GoodsInfo.builder()
                .goodsName(goods.getName())
                .goodsCode(goods.getCode())
                .description(goods.getDescription())
                .price(goods.getPrice())
                .discountRate(discountRate)
                .discountedPrice(discountedPrice)
                .build();
    }

}
