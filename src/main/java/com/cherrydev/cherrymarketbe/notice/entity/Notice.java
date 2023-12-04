package com.cherrydev.cherrymarketbe.notice.entity;

import com.cherrydev.cherrymarketbe.notice.enums.NoticeCategory;
import com.cherrydev.cherrymarketbe.notice.enums.NoticeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class Notice {
    private Long noticeId;
    private int code;
    private NoticeCategory category;
    private String subject;
    private String content;
    private NoticeStatus status;
    private Timestamp displayDate;
    private Timestamp hideDate;
    private Timestamp createNt;
    private Timestamp deleteNt;

    @Builder
    public Notice(Long noticeId, int code, NoticeCategory category, String subject, String content,
                  NoticeStatus status, Timestamp displayDate, Timestamp hideDate,Timestamp createNt, Timestamp deleteNt
                 ) {
        this.noticeId = noticeId;
        this.code = code;
        this.category = category;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.displayDate = displayDate;
        this.hideDate = hideDate;
        this.createNt = createNt;
        this.deleteNt = deleteNt;
    }
}
