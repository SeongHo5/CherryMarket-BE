package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;

import java.util.List;

public class UnavailableCartsFactory {

    public static UnavailableCarts create(List<Cart> carts) {

        return new UnavailableCarts(
                carts.stream()
                .filter(cart -> !cart.isGoodsAvailable())
                .map(CartDetails::getCarts)
                .toList()
        );
    }
}
