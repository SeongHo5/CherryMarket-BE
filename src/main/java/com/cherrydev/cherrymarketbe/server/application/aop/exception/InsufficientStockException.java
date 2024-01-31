package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import lombok.Getter;

@Getter
public class InsufficientStockException extends ApplicationException {

    public InsufficientStockException(ExceptionStatus status) {
        super(status);
    }
}
