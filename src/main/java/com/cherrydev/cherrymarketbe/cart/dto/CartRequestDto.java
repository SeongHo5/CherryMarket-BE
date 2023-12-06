package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.goods.entity.Goods;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
public record CartRequestDto(
        @NotNull Long accountId,
        @NotNull Long goodsId,
        @NotNull int quantity
) {

    public Cart addCart(Account account) {

        Goods goods = Goods.fromGoodsId(this.goodsId);

        return Cart.builderCreate()
                .accountId(account.getAccountId())
                .quantity(this.quantity)
                .goods(goods)
                .build();
    }
}
