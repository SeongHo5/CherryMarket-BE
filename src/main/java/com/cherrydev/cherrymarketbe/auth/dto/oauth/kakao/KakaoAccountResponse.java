package com.cherrydev.cherrymarketbe.auth.dto.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class KakaoAccountResponse {

    String id;
    String connectedAt;
    String synchedAt;

    @JsonProperty("kakao_account")
    KakaoAccount kakaoAccount;

    // getters, setters, constructors

    @Value
    public static class KakaoAccount {

        @JsonProperty("name_needs_agreement")
        Boolean nameNeedsAgreement;

        String name;

        @JsonProperty("has_email")
        Boolean hasEmail;

        @JsonProperty("email_needs_agreement")
        Boolean emailNeedsAgreement;

        @JsonProperty("is_email_valid")
        Boolean isEmailValid;

        @JsonProperty("is_email_verified")
        Boolean isEmailVerified;

        String email;

        @JsonProperty("has_phone_number")
        Boolean hasPhoneNumber;

        @JsonProperty("phone_number_needs_agreement")
        Boolean phoneNumberNeedsAgreement;

        @JsonProperty("phone_number")
        String phoneNumber;

        @JsonProperty("has_gender")
        Boolean hasGender;

        @JsonProperty("gender_needs_agreement")
        Boolean genderNeedsAgreement;

        String gender;
    }


}
