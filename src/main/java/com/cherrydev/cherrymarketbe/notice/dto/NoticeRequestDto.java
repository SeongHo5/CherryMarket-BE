package com.cherrydev.cherrymarketbe.notice.dto;


import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import com.cherrydev.cherrymarketbe.notice.enums.NoticeCategory;
import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;

import static com.cherrydev.cherrymarketbe.notice.enums.NoticeStatus.DISPLAY;


@Value
@Builder
public class NoticeRequestDto {

    int code;
    String category;
    String subject;
    String content;
    String status;
    String displayDate;
    String hideDate;



    public Notice toEntity() {
        return Notice.builder()
                .code(this.getCode())
                .category(NoticeCategory.valueOf(this.getCategory()))
                .subject(this.getSubject())
                .content(this.getContent())
                .status(DISPLAY)
                .displayDate(Timestamp.valueOf(this.getDisplayDate()))
                .hideDate(Timestamp.valueOf(this.getHideDate()))
                .build();
    }


}
