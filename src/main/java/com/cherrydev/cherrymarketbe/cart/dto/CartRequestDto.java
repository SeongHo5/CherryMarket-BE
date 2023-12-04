package com.cherrydev.cherrymarketbe.cart.dto;


import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.entity.TestGoods;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
public record CartRequestDto(
        @NotNull Long accountId,
        @NotNull Long goodsId,
        @NotNull Integer quantity
) {

    public Cart toEntity(TestGoods goods) {

        return Cart.builder()
                .accountId(this.accountId)
                .quantity(this.quantity)
                .goods(goods)
                .build();
    }

}
