package com.cherrydev.cherrymarketbe.common.constant;

public final class AuthConstant {

    // JWT Constants
    public static final String BLACK_LIST_KEY_PREFIX = "JWT::BLACK_LIST::";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30; // 30분
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7 * 2; // 2주

    // OAuth Constants - KAKAO
    public static final String KAKAO_AUTH_URL =  "https://kauth.kakao.com/oauth/token";
    public static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    public static final String KAKAO_USER_ADDRESS_URL = "https://kapi.kakao.com/v1/user/shipping_address";
    public static final String KAKAO_USER_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";

//    public static final String KAKAO_REDIRECT_URI = "http://social.s3-website.kr.object.ncloudstorage.com/login.html";
    public static final String KAKAO_REDIRECT_URI = "http://localhost:3000/oauth/callback/kakao";
    public static final String OAUTH_KAKAO_PREFIX = "OAUTH::KAKAO::";
    public static final String OAUTH_KAKAO_REFRESH_PREFIX = "OAUTH::KAKAO::REFRESH::";
    public static final String GRANT_TYPE_AUTHORIZATION = "authorization_code";

    // OAuth Constants - NAVER
    public static final String OAUTH_NAVER_PREFIX = "OAUTH::NAVER::";
    public static final String OAUTH_NAVER_REFRESH_PREFIX = "OAUTH::NAVER::REFRESH::";
    public static final String NAVER_AUTH_URL = "https://nid.naver.com/oauth2.0/token";
    public static final String NAVER_USER_INFO_URL = "https://openapi.naver.com/v1/nid/me";



    private AuthConstant() {
        throw new IllegalStateException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }
}
