package com.cherrydev.cherrymarketbe.notice.service;

import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoticeService {
    void createNotice(final NoticeRequestDto noticeRequestDto);

    ResponseEntity<NoticeInfoDto> getNoticeInfo(final Long noticeId);

    ResponseEntity<List<NoticeInfoDto>> findAll();

    void deleteNotice(Long noticeId);
}
