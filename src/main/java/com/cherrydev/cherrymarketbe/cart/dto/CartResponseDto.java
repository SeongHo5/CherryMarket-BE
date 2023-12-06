package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.entity.TestDiscount;
import com.cherrydev.cherrymarketbe.goods.entity.Goods;

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
        int discountRate
) {

    // TODO : Discount entity 불러오기

    public static CartResponseDto getCartsList(Cart cart) {

        Goods goods = cart.getGoods();
        TestDiscount discount = goods.getDiscount();


        //Goods에서
        Long discountId = Optional
                .ofNullable(discount)
                .map(TestDiscount::getDiscountId)
                .orElse(0L);

        String discountType = Optional
                .ofNullable(discount)
                .map(TestDiscount::getDiscountType)
                .orElse("");

        int discountRate = Optional
                .ofNullable(discount)
                .map(TestDiscount::getDiscountRate)
                .orElse(0);

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
