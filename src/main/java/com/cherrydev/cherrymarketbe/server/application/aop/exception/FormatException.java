package com.cherrydev.cherrymarketbe.server.application.aop.exception;

import lombok.Getter;

@Getter
public class FormatException extends ApplicationException {

    public FormatException(ExceptionStatus status) {
        super(status);
    }
}
