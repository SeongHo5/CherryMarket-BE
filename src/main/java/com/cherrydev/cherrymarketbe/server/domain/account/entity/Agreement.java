package com.cherrydev.cherrymarketbe.server.domain.account.entity;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agreement {

    private Long accountId;

    private Boolean termsOfService;

    private Boolean privacyPolicy;

    private Boolean marketing;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public static Agreement of(RequestSignUp request) {
        return Agreement.builder()
                .termsOfService(request.getServiceAgreement())
                .privacyPolicy(request.getPrivacyAgreement())
                .marketing(request.getMarketingAgreement())
                .build();
    }

}
