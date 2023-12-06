package com.cherrydev.cherrymarketbe.notice.service;

import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;

public interface NoticeService {
    void createNotice(final NoticeRequestDto noticeRequestDto);
}
