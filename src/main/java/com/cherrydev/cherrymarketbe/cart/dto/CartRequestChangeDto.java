package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CartRequestChangeDto(
        @NotNull Long cartId,
        @NotNull Integer quantity
) {

    public Cart toEntity() {

        return Cart.builderUpdate()
                .cartId(this.cartId)
                .quantity(this.quantity)
                .build();
    }


}
