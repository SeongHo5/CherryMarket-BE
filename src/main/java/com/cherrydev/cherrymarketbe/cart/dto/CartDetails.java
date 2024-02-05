package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.goods.dto.ToCartResponseDto;

public record CartDetails(
        Long cartId,
        Long goodsId,
        String salesStatus,
        String storageType,
        String goodsName,
        String goodsCode,
        Integer quantity,
        int price,
        Integer inventory,
        Integer discountRate,
        Integer discountedPrice
) {

    public static CartDetails getGoodsDetails(Cart cart) {

        ToCartResponseDto goods = cart.getGoods();

        return new CartDetails(
                cart.getCartId(),
                goods.getGoodsId(),
                goods.getSalesStatus(),
                goods.getStorageType(),
                goods.getGoodsName(),
                goods.getGoodsCode(),
                cart.getQuantity(),
                goods.getPrice(),
                goods.getInventory(),
                goods.getDiscountRate(),
                goods.getDiscountedPrice()
        );

    }

}
