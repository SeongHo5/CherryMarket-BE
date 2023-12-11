package com.cherrydev.cherrymarketbe.goods.exception;

import com.cherrydev.cherrymarketbe.goods.exception.enums.GoodsExceptionStatus;
import lombok.Getter;

@Getter
public class FormatException extends RuntimeException {

    private final int statusCode;
    private final String message;

    public FormatException(GoodsExceptionStatus status) {
        this.statusCode = status.getStatusCode();
        this.message = status.getMessage();
    }
}
