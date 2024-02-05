package com.cherrydev.cherrymarketbe.notice.dto;


import com.cherrydev.cherrymarketbe.common.utils.TimeFormatter;
import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;


@Getter
@NoArgsConstructor(force = true)
public class NoticeInfoDto {
    Long noticeId;
    String code;
    String category;
    String subject;
    String content;
    String status;
    String displayDate;
    String hideDate;
    String createDate;
    String deleteDate;

    public NoticeInfoDto(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.code = notice.getCode();
        this.category = notice.getCategory().toString();
        this.subject = notice.getSubject();
        this.content = notice.getContent();
        this.status = notice.getStatus().toString();
        this.displayDate = notice.getDisplayDate() != null ? TimeFormatter.timeStampToString(Timestamp.valueOf(notice.getDisplayDate().toLocalDateTime())) : null;
        this.hideDate = notice.getHideDate() != null ? TimeFormatter.timeStampToString(Timestamp.valueOf(notice.getHideDate().toLocalDateTime())) : null;
        this.createDate = notice.getCreateDate() != null ? TimeFormatter.timeStampToString(Timestamp.valueOf(notice.getCreateDate().toLocalDateTime())) : null;
        this.deleteDate = notice.getDeleteDate() != null ? TimeFormatter.timeStampToString(Timestamp.valueOf(notice.getDeleteDate().toLocalDateTime())) : null;
    }

    public static List<NoticeInfoDto> convertToDtoList(List<Notice> noticeList) {
        return noticeList.stream()
                .map(NoticeInfoDto::new)
                .toList();
    }
}
