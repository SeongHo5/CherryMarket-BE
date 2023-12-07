package com.cherrydev.cherrymarketbe.notice.service;

import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoticeService {

    void createNotice(final NoticeRequestDto noticeRequestDto);

    ResponseEntity<NoticeInfoDto> getNoticeInfo(final Long noticeId);

    ResponseEntity<NoticeInfoDto> getNoticeInfoByCode(String noticeCode);

    ResponseEntity<List<NoticeInfoDto>> findAll();

    ResponseEntity<NoticeInfoDto> modifyById(ModifyNoticeInfoRequestDto requestDto);

    ResponseEntity<NoticeInfoDto> modifyByCode(ModifyNoticeInfoRequestDto requestDto);

    void deleteById(Long noticeId);

    void deleteByCode(String code);


}
