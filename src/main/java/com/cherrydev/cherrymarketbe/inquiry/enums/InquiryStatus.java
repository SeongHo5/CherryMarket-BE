package com.cherrydev.cherrymarketbe.inquiry.enums;

import lombok.Getter;

@Getter
public enum InquiryStatus {

    ACCEPTING("접수중"),
    COMPLETE("답변완료");


    private final String status;
    InquiryStatus(String status) {
        this.status = status;
    }
}
