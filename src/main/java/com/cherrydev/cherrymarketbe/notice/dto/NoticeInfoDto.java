package com.cherrydev.cherrymarketbe.notice.dto;


import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import lombok.Getter;

import java.util.List;

import static com.cherrydev.cherrymarketbe.common.utils.TimeFormatter.timeStampToString;


@Getter
public class NoticeInfoDto {
    long noticeId;
    String code;
    String category;
    String subject;
    String content;
    String status;
    String displayDate;
    String hideDate;
    String createNt;
    String deleteNt;

    public NoticeInfoDto(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.code = notice.getCode();
        this.category = notice.getCategory().toString();
        this.subject = notice.getSubject();
        this.content = notice.getContent();
        this.status = notice.getStatus().toString();
        this.displayDate = timeStampToString(notice.getDisplayDate());
        this.hideDate = timeStampToString(notice.getHideDate());
        this.createNt = timeStampToString(notice.getCreateNt());
        this.deleteNt = notice.getDeleteNt() != null ? timeStampToString(notice.getDeleteNt()) : null;
    }
    public static List<NoticeInfoDto> convertToDtoList(List<Notice> noticeList) {
        return noticeList.stream()
                .map(NoticeInfoDto::new)
                .toList();
    }
}
