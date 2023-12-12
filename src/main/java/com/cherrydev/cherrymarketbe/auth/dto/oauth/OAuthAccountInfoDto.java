package com.cherrydev.cherrymarketbe.auth.dto.oauth;

import com.cherrydev.cherrymarketbe.account.enums.Gender;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.kakao.KakaoAccountResponse;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.naver.NaverAccountResponse;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.INVALID_INPUT_VALUE;


@Value
@NoArgsConstructor(force = true)
public class OAuthAccountInfoDto {

    @NotNull
    String id;

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

    public OAuthAccountInfoDto(NaverAccountResponse naverAccount) {
        NaverAccountResponse.NaverAccount naverAccountResponse = naverAccount.getNaverAccount();
        this.id = naverAccountResponse.getId();
        this.name = naverAccountResponse.getName();
        this.email = naverAccountResponse.getEmail();
        this.contact = naverAccountResponse.getContact();
        this.gender = toEnum(naverAccountResponse.getGender());
    }

    private Gender toEnum(String gender) {
        if (gender.equals("M")) {
            return Gender.MALE;
        }
        if (gender.equals("F")) {
            return Gender.FEMALE;
        }
        throw new NotFoundException(INVALID_INPUT_VALUE);
    }
}
