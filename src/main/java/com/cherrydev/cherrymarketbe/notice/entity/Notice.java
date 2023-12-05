package com.cherrydev.cherrymarketbe.notice.entity;

import com.cherrydev.cherrymarketbe.notice.enums.NoticeCategory;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class Notice {
    private Long noticeId;
    private String code;
    private NoticeCategory category;
    private String subject;
    private String content;
    private DisplayStatus status;
    private Timestamp displayDate;
    private Timestamp hideDate;
    private Timestamp createDate;
    private Timestamp deleteDate;

    @Builder
    public Notice(Long noticeId, String code, NoticeCategory category, String subject, String content,
                  DisplayStatus status, Timestamp displayDate, Timestamp hideDate,Timestamp createDate, Timestamp deleteDate
                 ) {
        this.noticeId = noticeId;
        this.code = code;
        this.category = category;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.displayDate = displayDate;
        this.hideDate = hideDate;
        this.createDate = createDate;
        this.deleteDate = deleteDate;
    }

    public void updateCategory(NoticeCategory category) {
        this.category = category;
    }

    public void updateSubject(String subject) {
        this.subject = subject;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateStatus(DisplayStatus status) {
        this.status = status;
    }
}
