package com.cherrydev.cherrymarketbe.common.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CherryLogger {

    private CherryLogger() {
        throw new IllegalStateException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static void logRequestInfo(HttpServletRequest request) {
        log.info("[REQUEST] {} {} / FROM {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr()
        );
    }

    public static void logServerStart() {
        log.info("============================================================================");
        log.info("*********************** SERVER SUCCESSFULLY STARTED ***********************");
        log.info("============================================================================");
    }

}
