package com.cherrydev.cherrymarketbe.customer.enums;

import lombok.Getter;

@Getter
public enum RewardGrantType {

        WELCOME("회원가입"),
        PURCHASE("구매"),
        REVIEW("리뷰"),
        EVENT("이벤트"),
        ADMIN("관리자");

        private final String description;

        RewardGrantType(String description) {
            this.description = description;
        }
}
