package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record CartsByStorageType(
        Map<String, List<CartDetails>> itemsByType
) {

    public CartsByStorageType(List<Cart> carts) {
        this(
                carts.stream()
                        .filter(Cart::isGoodsAvailable)
                        .map(CartDetails::addGoods)
                        .collect(Collectors.groupingBy(CartDetails::storageType))
        );
    }

}
