package com.cherrydev.cherrymarketbe.common.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 무작위 코드를 생성하는 유틸리티 클래스입니다.
 * <p>
 * 인증 코드 생성, 임시 비밀번호 생성 등에 사용할 수 있습니다.
 */
public class CodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random random = new SecureRandom();

    /**
     * SecureRandom을 이용하여 무작위 코드를 생성합니다.
     */
    public static String generateRandomCode(final int requiredLength) {
        StringBuilder sb = new StringBuilder(requiredLength);
        for (int i = 0; i < requiredLength; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

}
