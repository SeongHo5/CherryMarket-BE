package com.cherrydev.cherrymarketbe.notice.dto;

import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import com.cherrydev.cherrymarketbe.notice.enums.NoticeCategory;
import lombok.Builder;
import lombok.Value;
import java.sql.Timestamp;

@Value
@Builder
public class NoticeRequestDto {
    String code;
    String category;
    String subject;
    String content;
    String status;
    String displayDate;
    String hideDate;

    public Notice toEntity() {
        return Notice.builder()
                .code("001")
                .category(NoticeCategory.valueOf(this.getCategory()))
                .subject(this.getSubject())
                .content(this.getContent())
                .status(DisplayStatus.ACTIVE)
                .displayDate(Timestamp.valueOf(this.getDisplayDate()))
                .hideDate(Timestamp.valueOf(this.getHideDate()))
                .build();
    }

}
