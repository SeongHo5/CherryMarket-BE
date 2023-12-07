package com.cherrydev.cherrymarketbe.notice.enums;

import lombok.Getter;

@Getter
public enum NoticeCategory {
    EVENT("이벤트"),
    ANNOUNCEMENT("안내");

    private final String category;
    NoticeCategory(String category) {
        this.category = category;
    }
}
