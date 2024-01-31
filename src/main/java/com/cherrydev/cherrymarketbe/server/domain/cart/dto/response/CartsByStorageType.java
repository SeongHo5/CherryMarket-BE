package com.cherrydev.cherrymarketbe.server.domain.cart.dto.response;

import com.cherrydev.cherrymarketbe.server.domain.cart.entity.Cart;

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
                        .map(CartDetails::getGoodsDetails)
                        .collect(Collectors.groupingBy(CartDetails::storageType))
        );
    }

}
