package com.cherrydev.cherrymarketbe.server.application.common.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class TimeFormatter {

    private TimeFormatter() {
        throw new IllegalStateException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    /**
     * LocalDate를 String으로 변환
     */
    public static String localDateToString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * Timestamp를 String으로 변환
     */
    public static String timeStampToString(java.sql.Timestamp timestamp) {
        return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
