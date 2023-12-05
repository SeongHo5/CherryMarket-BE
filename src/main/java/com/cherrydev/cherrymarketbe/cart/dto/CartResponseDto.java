package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.entity.TestDiscount;
import com.cherrydev.cherrymarketbe.cart.entity.TestGoods;

public record CartResponseDto(
        Long cartId,
        Long goodsId,
        String salesStatus,
        String storageType,
        String goodsName,
        Integer quantity,
        Integer price,
        Integer inventory,
        Long discountId,
        String discountType,
        Integer disountRate
) {

    // TODO : Goods, Discount entity 불러오기

    public static CartResponseDto createCartListItemResponse(Cart cart) {

        TestGoods goods = cart.getGoods();
        TestDiscount discount = goods.getDiscount();

        return new CartResponseDto(
                cart.getCartId(),
                goods.getGoodsId(),
                goods.getSalesStatus(),
                goods.getStorageType(),
                goods.getGoodsName(),
                cart.getQuantity(),
                goods.getPrice(),
                goods.getInventory(),
                discount.getDiscountId(),
                discount.getDiscountType(),
                discount.getDiscountRate()
        );

    }

}
