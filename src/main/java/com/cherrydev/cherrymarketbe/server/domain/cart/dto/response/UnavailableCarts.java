package com.cherrydev.cherrymarketbe.server.domain.cart.dto.response;

import java.util.List;

public record UnavailableCarts(List<CartDetails> cartItems) {
}
