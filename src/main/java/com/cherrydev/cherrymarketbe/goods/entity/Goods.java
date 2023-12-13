package com.cherrydev.cherrymarketbe.goods.entity;

import com.cherrydev.cherrymarketbe.cart.entity.TestDiscount;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class Goods {

    private Long goodsId;
    private String goodsName;
    private String description;
    private int price;
    private int retailPrice;
    private int inventory;
    private String storageType;
    private String capacity;
    private Timestamp expDate;
    private String allergyInfo;
    private String originPlace;
    private String salesStatus;

    private TestDiscount discount;

    @Builder
    public Goods(Long goodsId, String goodsName, String description, int price, int retailPrice, int inventory, String storageType, String capacity,
                 Timestamp expDate, String allergyInfo, String originPlace, String salesStatus

            ,TestDiscount discount) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.description = description;
        this.price = price;
        this.retailPrice = retailPrice;
        this.inventory = inventory;
        this.storageType = storageType;
        this.capacity = capacity;
        this.expDate = expDate;
        this.allergyInfo = allergyInfo;
        this.originPlace = originPlace;
        this.salesStatus = salesStatus;
        this.discount = discount;
    }

    public static Goods fromGoodsId(Long goodsId) {
        Goods goods = new Goods();
        goods.goodsId = goodsId;
        return goods;
    }
}