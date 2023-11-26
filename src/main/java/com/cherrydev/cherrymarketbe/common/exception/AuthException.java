package com.cherrydev.cherrymarketbe.common.exception;

import com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus;
import lombok.Getter;


@Getter
public class AuthException extends RuntimeException {

    private final int statusCode;
    private final String message;

    public AuthException(ExceptionStatus status) {
        this.statusCode = status.getStatusCode();
        this.message = status.getMessage();
    }
}
