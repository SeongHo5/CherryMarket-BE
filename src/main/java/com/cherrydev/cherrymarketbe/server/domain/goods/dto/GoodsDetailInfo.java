package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import lombok.Builder;

import java.util.List;

@Builder
public record GoodsDetailInfo(
        Long goodsId, String goodsCode, String goodsName, String description, int price,
        int inventory, String storageType, String capacity, String expDate, String allergyInfo,
        String originPlace, String salesStatus, Integer discountRate, Integer discountedPrice,
        String makerName
) {

    public static GoodsDetailInfo of(Goods goods) {
            return GoodsDetailInfo.builder()
                .goodsId(goods.getId())
                .goodsCode(goods.getCode())
                .goodsName(goods.getName())
                .description(goods.getDescription())
                .price(goods.getPrice())
                .inventory(goods.getInventory())
                .storageType(goods.getCustodyType())
                .capacity(goods.getCapacity())
                .expDate(goods.getExpiredAt())
                .allergyInfo(goods.getAllergyInformation())
                .originPlace(goods.getOriginPlace())
                .salesStatus(goods.getSalesStatus())
                .discountRate(goods.getDiscountRate())
                .discountedPrice((int) (goods.getPrice() * (1 - (goods.getDiscountRate() / 100.0))))
                .makerName(goods.getMakerName())
                .build(); }

}
