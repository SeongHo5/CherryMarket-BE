package com.cherrydev.cherrymarketbe.cart.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class Cart {

    private Long cartId;

    private Long accountId;

    private Integer quantity;

    private TestGoods goods;

    //private Discount discount;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Builder
    public Cart(Long accountId, Integer quantity
                ,TestGoods goods
            //, Discount discount,
                ) {
        this.accountId = accountId;
        this.quantity = quantity;
        this.goods = goods;
//        this.discount = discount;
    }

    public Cart updateGoodsQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

}
