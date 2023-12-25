package com.cherrydev.cherrymarketbe.auth.dto.oauth;

import com.cherrydev.cherrymarketbe.account.enums.Gender;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.kakao.KakaoAccountResponse;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.naver.NaverAccountResponse;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.INVALID_INPUT_VALUE;


@Value
@NoArgsConstructor(force = true)
public class OAuthAccountInfoDto2 {

    @NotNull
    String id;

    @NotNull
    String name;


    @Builder
    public OAuthAccountInfoDto2(@JsonProperty("id") String id,
                                @JsonProperty("properties.nickname") String nickname){
        this.id = id;
        this.name = nickname;

//        this.gender = Gender.valueOf(kakaoAccount.getGender().toUpperCase());
    }

}
