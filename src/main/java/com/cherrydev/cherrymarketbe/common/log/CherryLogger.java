package com.cherrydev.cherrymarketbe.common.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CherryLogger {

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
