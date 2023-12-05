package com.cherrydev.cherrymarketbe.goods.dto;

import com.cherrydev.cherrymarketbe.goods.entity.Goods;
import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;

@Value
@Builder
public class GoodsDto {

    Long goodsId;
    String goodsName;
    String description;
    int price;
    int retailPrice;
    int inventory;
    String storageType;
    String capacity;
    Timestamp expDate;
    String allergyInfo;
    String originPlace;
    String salesStatus;


    public Goods toEntity() {
        return Goods.builder()
                       .goodsId(this.getGoodsId())
                       .goodsName(this.getGoodsName())
                       .description(this.getDescription())
                       .price(this.getPrice())
                       .retailPrice(this.getRetailPrice())
                       .inventory(this.getInventory())
                       .storageType(this.getStorageType())
                       .capacity(this.getCapacity())
                       .expDate(this.getExpDate())
                       .allergyInfo(this.getAllergyInfo())
                       .originPlace(this.getOriginPlace())
                       .salesStatus(this.getSalesStatus())
                       .build();
    }
}
