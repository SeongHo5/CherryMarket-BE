package com.cherrydev.cherrymarketbe.common.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {

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
