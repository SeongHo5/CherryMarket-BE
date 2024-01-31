package com.cherrydev.cherrymarketbe.server.domain.cart.dto.request;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.ToCartResponse;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
public record AddCart(
        @NotNull Long goodsId,
        @NotNull Integer quantity
) {

    public Cart getCart(AccountDetails accountDetails, ToCartResponse responseDto) {

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
