package com.cherrydev.cherrymarketbe.order.domain;

import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import jakarta.validation.constraints.NotNull;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.INVALID_INPUT_VALUE;

public record GoodsInfo(
        @NotNull Long goodsId,
        @NotNull Integer quantity,
        String goodsCode

) {

    public GoodsInfo {
        if (quantity <= 0) {
            throw new ServiceFailedException(INVALID_INPUT_VALUE);
        }
    }
}
