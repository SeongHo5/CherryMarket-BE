package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import lombok.Getter;

@Getter
public class OnSaleGoodsException extends ApplicationException {


    public OnSaleGoodsException(ExceptionStatus status) {
        super(status);
    }
}
