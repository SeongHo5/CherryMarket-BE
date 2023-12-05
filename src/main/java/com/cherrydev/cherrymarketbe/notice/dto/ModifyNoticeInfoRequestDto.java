package com.cherrydev.cherrymarketbe.notice.dto;

import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import lombok.Getter;

@Getter
public class ModifyNoticeInfoRequestDto {
//    long noticeId;
    String code;
    String category;
    String subject;
    String content;
    String displayDate;
    String hideDate;



    public ModifyNoticeInfoRequestDto( String category, String subject, String content, String displayDate, String hideDate) {
//        this.code = code;
        this.category = category;
        this.subject = subject;
        this.content = content;
        this.displayDate = displayDate;
        this.hideDate = hideDate;
    }


}
