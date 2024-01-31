package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import lombok.Getter;

@Getter
public class DiscontinuedGoodsException extends ApplicationException {

    public DiscontinuedGoodsException(ExceptionStatus status) {
        super(status);
    }
}
