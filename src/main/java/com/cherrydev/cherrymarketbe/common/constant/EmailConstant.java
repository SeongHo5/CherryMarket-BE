package com.cherrydev.cherrymarketbe.common.constant;

/**
 * 이메일 기능 관련 상수
 */
public final class EmailConstant {

    public static final String PREFIX_VERIFY = "EMAIL::VERIFY::";
    public static final String PREFIX_VERIFIED = "EMAIL::VERIFIED::";
    public static final String PREFIX_PW_RESET = "EMAIL::PW_RESET::";
    public static final String VALUE_TRUE = "TRUE";

    public static final String ENCODING_CHARSET = "UTF-8";
    public static final String MAILER_SUBTYPE = "HTML";

    public static final int VERIFICATION_CODE_LENGTH = 6;
    public static final Long VERIFICATION_CODE_EXPIRE_TIME = 60 * 3L; // 3분
    public static final Long WHITE_LIST_VERIFIED_TIME = 60 * 60 * 24L; // 1일

}
