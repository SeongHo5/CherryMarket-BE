package com.cherrydev.cherrymarketbe.cart.dto;

import java.util.List;

public record UnavailableCarts(
        List<CartDetails> cartItems
) {
}
