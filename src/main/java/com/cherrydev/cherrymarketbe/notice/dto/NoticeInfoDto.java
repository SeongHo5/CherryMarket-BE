package com.cherrydev.cherrymarketbe.notice.dto;


import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import lombok.Getter;

import static com.cherrydev.cherrymarketbe.common.utils.TimeFormatter.timeStampToString;



@Getter
public class NoticeInfoDto {
    long noticeId;
    int code;
//    NoticeCategory category;
    String category;
    String subject;
    String content;
//    NoticeStatus status;
    String status;
    String displayDate;
    String hideDate;
    String createNt;


  public NoticeInfoDto(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.code = notice.getCode();
        this.category = notice.getCategory().toString();
        this.subject = notice.getSubject();
        this.content = notice.getContent();
        this.status = notice.getStatus().toString();
        this.displayDate = timeStampToString(notice.getDisplayDate());
        this.hideDate = timeStampToString(notice.getHideDate()) ;
        this.createNt = timeStampToString(notice.getCreateNt()) ;
    }
}
