package com.cherrydev.cherrymarketbe.cart.dto;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus;
import com.cherrydev.cherrymarketbe.goods.entity.Goods;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.sf.jsqlparser.util.validation.ValidationException;

@Builder
public record AddCart(
        @NotNull Long accountId,
        @NotNull Long goodsId,
        @NotNull Integer quantity
) {

    public Cart addCart(Account account) {

        validateFields();

        Goods goods = Goods.fromGoodsId(this.goodsId);

        return Cart.builder()
                .accountId(account.getAccountId())
                .quantity(this.quantity)
                .goods(goods)
                .build();
    }

    private void validateFields() {
        if (this.accountId == null) {
            throw new NotFoundException(ExceptionStatus.NOT_FOUND_ACCOUNT);
        }
        if (this.goodsId == null) {
            throw new NotFoundException(ExceptionStatus.NOT_FOUND_GOODS);
        }
        if (this.quantity == null || this.quantity <= 0) {
            throw new ServiceFailedException(ExceptionStatus.INVALID_INPUT_VALUE);
        }
    }

}
