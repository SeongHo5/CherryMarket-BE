package com.cherrydev.cherrymarketbe.notice.enums;

import lombok.Getter;

@Getter
public enum DisplayStatus {

    ACTIVE("게시"),
    DELETED("비게시"),
    BLINDED ("신고누적");


    private final String status;
    DisplayStatus(String status) {
        this.status = status;
    }
}
