package com.cherrydev.cherrymarketbe.server.domain.auth.dto.response;

import com.cherrydev.cherrymarketbe.server.domain.account.enums.UserRole;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SignInResponse {

    String userName;
    UserRole userRole;
    String grantType;
    String accessToken;
    String refreshToken;
    Long expiresIn;

}
