package com.cherrydev.cherrymarketbe.server.domain.cart.dto.response;

import com.cherrydev.cherrymarketbe.server.domain.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.ToCartResponse;

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

        ToCartResponse goods = cart.getGoods();

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
