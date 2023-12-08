package com.cherrydev.cherrymarketbe.cart.entity;

import com.cherrydev.cherrymarketbe.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.goods.enums.SalesStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Cart {

    private Long cartId;

    private Long accountId;

    private Integer quantity;

    private Goods goods;

    @Builder
    public Cart(Long cartId, Long accountId, Integer quantity, Goods goods) {
        this.cartId = cartId;
        this.accountId = accountId;
        this.quantity = quantity;
        this.goods = goods;
    }

    public boolean isGoodsAvailable() {
        return SalesStatus.ON_SALE.name().equals(this.getGoods().getSalesStatus());
    }

}
