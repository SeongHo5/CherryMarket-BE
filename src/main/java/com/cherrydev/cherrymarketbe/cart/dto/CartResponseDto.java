package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.entity.TestDiscount;
import com.cherrydev.cherrymarketbe.cart.entity.TestGoods;

import java.util.Optional;

public record CartResponseDto(
        Long cartId,
        Long goodsId,
        String salesStatus,
        String storageType,
        String goodsName,
        int quantity,
        int price,
        Integer inventory,
        Long discountId,
        String discountType,
        Double discountRate
) {

    // TODO : Goods, Discount entity 불러오기

    public static CartResponseDto getCartsList(Cart cart) {

        TestGoods goods = cart.getGoods();
        TestDiscount discount = goods.getDiscount();

        Long discountId = Optional
                .ofNullable(discount)
                .map(TestDiscount::getDiscountId)
                .orElse(0L);

        String discountType = Optional
                .ofNullable(discount)
                .map(TestDiscount::getDiscountType)
                .orElse("");

        Double discountRate = Optional
                .ofNullable(discount)
                .map(TestDiscount::getDiscountRate)
                .orElse(0.0);

        return new CartResponseDto(
                cart.getCartId(),
                goods.getGoodsId(),
                goods.getSalesStatus(),
                goods.getStorageType(),
                goods.getGoodsName(),
                cart.getQuantity(),
                goods.getPrice(),
                goods.getInventory(),
                discountId,
                discountType,
                discountRate
        );

    }

}
