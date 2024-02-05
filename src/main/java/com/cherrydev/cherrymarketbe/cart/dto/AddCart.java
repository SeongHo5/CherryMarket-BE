package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus;
import com.cherrydev.cherrymarketbe.goods.dto.ToCartResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
public record AddCart(
        @NotNull Long goodsId,
        @NotNull Integer quantity
) {

    public Cart getCart(AccountDetails accountDetails, ToCartResponseDto responseDto) {

        validateFields();

        return Cart.builder()
                .accountId(accountDetails.getAccount().getAccountId())
                .quantity(this.quantity)
                .goods(responseDto)
                .build();


    }

    private void validateFields() {
        if (this.goodsId == null) {
            throw new NotFoundException(ExceptionStatus.NOT_FOUND_GOODS);
        }
        if (this.quantity == null || this.quantity <= 0) {
            throw new ServiceFailedException(ExceptionStatus.INVALID_INPUT_VALUE);
        }
    }

}
