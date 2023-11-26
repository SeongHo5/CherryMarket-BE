package com.cherrydev.cherrymarketbe.auth.dto.oauth;

import jakarta.validation.constraints.Pattern;
import lombok.Value;

@Value
public class OAuthRequestDto {

    String authCode;

    @Pattern(regexp = "NAVER|KAKAO|GOOGLE", message = "지원하지 않는 OAuth 제공자입니다.")
    String provider;
}
