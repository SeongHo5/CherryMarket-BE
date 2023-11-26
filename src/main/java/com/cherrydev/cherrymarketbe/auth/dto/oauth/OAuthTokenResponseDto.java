package com.cherrydev.cherrymarketbe.auth.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class OAuthTokenResponseDto {

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("refresh_token")
    String refreshToken;

    @JsonProperty("expires_in")
    Long expiresIn;

    String scope;

    @JsonProperty("refresh_token_expires_in")
    Long refreshTokenExpiresIn;
}
