package com.cherrydev.cherrymarketbe.auth.dto.oauth;

import com.cherrydev.cherrymarketbe.account.enums.Gender;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.kakao.KakaoAccountResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;


@Value
@NoArgsConstructor(force = true)
public class OAuthAccountInfoDto {

    @NotNull
    Long id;

    @NotNull
    String name;

    @NotNull
    String email;

    @Nullable
    String contact;

    @Nullable
    Gender gender;

    @Builder
    public OAuthAccountInfoDto(KakaoAccountResponse kakaoResponse, KakaoAccountResponse.KakaoAccount kakaoAccount) {
        this.id = kakaoResponse.getId();
        this.name = kakaoAccount.getName();
        this.email = kakaoAccount.getEmail();
        this.contact = kakaoAccount.getPhoneNumber();
        this.gender = Gender.valueOf(kakaoAccount.getGender().toUpperCase());
    }
}
