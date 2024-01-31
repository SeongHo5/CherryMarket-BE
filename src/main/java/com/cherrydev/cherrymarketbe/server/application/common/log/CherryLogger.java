package com.cherrydev.cherrymarketbe.server.application.common.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

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

    public static void logExceptionDetail(HttpServletRequest request, Exception e) {
        StringBuilder parameters = new StringBuilder();
        request.getParameterMap().forEach((key, value)
                -> parameters.append(key).append("=").append(Arrays.toString(value)).append(", "));
        log.error("[EXCEPTION] {} {} / FROM {} / PARAMS: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                parameters,
                e
        );
    }

    public static void logUnAuthorizedRequest(HttpServletRequest request) {
        StringBuilder parameters = new StringBuilder();
        request.getParameterMap().forEach((key, value)
                -> parameters.append(key).append("=").append(Arrays.toString(value)).append(", "));
        log.warn("[UNAUTHORIZED REQUEST] {} {} / FROM {} / PARAMS: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                parameters
        );
    }

    public static void logForbiddenRequest(HttpServletRequest request) {
        StringBuilder parameters = new StringBuilder();
        request.getParameterMap().forEach((key, value)
                -> parameters.append(key).append("=").append(Arrays.toString(value)).append(", "));
        log.warn("[FORBIDDEN REQUEST] {} {} / FROM {} / PARAMS: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                parameters
        );
    }

    public static void logServerStart() {
        log.info("============================================================================");
        log.info("*********************** SERVER SUCCESSFULLY STARTED ***********************");
        log.info("============================================================================");
    }

}
