package com.cherrydev.cherrymarketbe.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class Agreement {

    private Long accountId;

    private Boolean termsOfService;

    private Boolean privacyPolicy;

    private Boolean marketing;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
