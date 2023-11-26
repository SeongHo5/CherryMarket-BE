package com.cherrydev.cherrymarketbe.common.exception;

import lombok.Getter;
import com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus;

@Getter
public class ServiceFailedException extends RuntimeException {

    private final int statusCode;
    private final String message;

    public ServiceFailedException(ExceptionStatus ex) {
        this.statusCode = ex.getStatusCode();
        this.message = ex.getMessage();
    }
}
