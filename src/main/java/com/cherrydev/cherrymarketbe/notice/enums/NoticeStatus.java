package com.cherrydev.cherrymarketbe.notice.enums;

import lombok.Getter;

@Getter
public enum NoticeStatus {

    DISPLAY("게시"),
    HIDE("비게시");


    private final String status;
    NoticeStatus(String status) {
        this.status = status;
    }
}
