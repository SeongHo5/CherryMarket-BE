package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChangeCart(
        @NotNull Long cartId,
        @NotNull Integer quantity

) {

    public Cart toEntity() {

        return Cart.builder()
                .cartId(this.cartId)
                .quantity(this.quantity)
                .build();
    }

}
