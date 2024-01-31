package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import jakarta.validation.constraints.NotNull;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.INVALID_INPUT_VALUE;

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
