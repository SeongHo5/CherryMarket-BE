package com.cherrydev.cherrymarketbe.server.domain.cart.dto.response;

import com.cherrydev.cherrymarketbe.server.domain.cart.entity.Cart;

import java.util.List;

public class UnavailableCartsFactory {

    public static UnavailableCarts create(List<Cart> carts) {

        return new UnavailableCarts(
                carts.stream()
                        .filter(cart -> !cart.isGoodsAvailable())
                        .map(CartDetails::getGoodsDetails)
                        .toList()
        );
    }
}
