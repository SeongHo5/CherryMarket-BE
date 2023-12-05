package com.cherrydev.cherrymarketbe.notice.service;

import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NoticeService {
    void createNotice(final NoticeRequestDto noticeRequestDto);

    ResponseEntity<NoticeInfoDto> getNoticeInfo(final Long noticeId);
    ResponseEntity<NoticeInfoDto> getNoticeInfoByCode(String noticeCode);

    ResponseEntity<List<NoticeInfoDto>> findAll();

    ResponseEntity<NoticeInfoDto> modifyNotice(ModifyNoticeInfoRequestDto requestDto, final Long noticeId);
//    void modifyNotice(ModifyNoticeInfoRequestDto requestDto, final Long noticeId);
    void deleteNotice(Long noticeId);


}
