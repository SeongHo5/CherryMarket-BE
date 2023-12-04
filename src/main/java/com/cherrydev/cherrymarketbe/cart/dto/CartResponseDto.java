package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.entity.TestGoods;
import com.cherrydev.cherrymarketbe.cart.enums.CustodyType;

import java.math.BigDecimal;

public record CartResponseDto(
        Long accountId,
        Integer quantity,
        Long goodsId,
        String goodsName,
        BigDecimal goodsPrice,
        CustodyType custodyType
//        Long discountId,
//        BigDecimal discountRate
) {

    // TODO : Goods, Discount entity 불러오기

    public static CartResponseDto fromCart(Cart cart) {
        TestGoods goods = cart.getGoods();

        //Discount discount = cart.getDiscount();

        return new CartResponseDto(
                cart.getAccountId(),
                cart.getQuantity(),
                goods.getGoodsId(),
                goods.getGoodsName(),
                goods.getPrice(),
                goods.getCustodyType());
    }

}
