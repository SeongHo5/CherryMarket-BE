package com.cherrydev.cherrymarketbe.auth.constants;

public class OAuthConstant {

    public static final String KAKAO_AUTH_URL =  "https://kauth.kakao.com/oauth/token";
    public static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    public static final String KAKAO_USER_ADDRESS_URL = "https://kapi.kakao.com/v1/user/shipping_address";
    public static final String KAKAO_USER_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";
    public static final String KAKAO_REDIRECT_URI = "http://localhost:8080/callback";
    public static final String OAUTH_KAKAO_PREFIX = "OAUTH::KAKAO::";
    public static final String OAUTH_KAKAO_REFRESH_PREFIX = "OAUTH::KAKAO::REFRESH::";
    public static final String OAUTH_KAKAO_GRANT_TYPE = "authorization_code";


}
