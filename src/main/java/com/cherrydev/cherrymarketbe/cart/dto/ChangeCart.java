package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChangeCart(
        @NotNull Long cartId,
        @NotNull Integer quantity
) {

    public Cart toEntity() {

        validateFields();

        return Cart.builder()
                .cartId(this.cartId)
                .quantity(this.quantity)
                .build();
    }

    private void validateFields() {
        if (this.cartId == null) {
            throw new NotFoundException(ExceptionStatus.INVALID_INPUT_VALUE);
        }
        if (this.quantity == null || this.quantity <= 0) {
            throw new ServiceFailedException(ExceptionStatus.INVALID_INPUT_VALUE);
        }
    }

}
