package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.entity.TestDiscount;
import com.cherrydev.cherrymarketbe.goods.entity.Goods;

import java.util.Optional;

public record CartDetails(
        Long cartId,
        Long goodsId,
        String salesStatus,
        String storageType,
        String goodsName,
        Integer quantity,
        int price,
        Integer inventory,
        Long discountId,
        String discountType,
        int discountRate
) {

    public static CartDetails addGoods(Cart cart) {

        Goods goods = cart.getGoods();
        TestDiscount discount = goods.getDiscount();

        // TODO: 추후 Discount entity 참조 연결. Goods에서 Discount null 처리 시 아래 코드 삭제 예정

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

        return new CartDetails(
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
