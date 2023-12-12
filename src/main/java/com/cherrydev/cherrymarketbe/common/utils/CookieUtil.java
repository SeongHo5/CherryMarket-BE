package com.cherrydev.cherrymarketbe.common.utils;

import org.springframework.http.ResponseCookie;

import static com.cherrydev.cherrymarketbe.common.config.SecurityConfig.ALLOWED_ORIGINS;
import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.REFRESH_TOKEN_EXPIRE_TIME;

public class CookieUtil {

    private CookieUtil() {
        throw new IllegalStateException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static ResponseCookie createCookie(String cookieName, String cookieValue) {

        return ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .secure(true)
                .path("/")
//                .domain(ALLOWED_ORIGINS)
                .maxAge(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

}
