package com.cherrydev.cherrymarketbe.goods.exception;

import com.cherrydev.cherrymarketbe.goods.exception.enums.GoodsExceptionStatus;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final int statusCode;
    private final String message;

    public NotFoundException(GoodsExceptionStatus status) {
        this.statusCode = status.getStatusCode();
        this.message = status.getMessage();
    }
}
