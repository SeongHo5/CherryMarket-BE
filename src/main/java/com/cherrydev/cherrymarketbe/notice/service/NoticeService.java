package com.cherrydev.cherrymarketbe.notice.service;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeInfoDto;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface NoticeService {

    void createNotice(final NoticeRequestDto noticeRequestDto);

    ResponseEntity<NoticeInfoDto> getNoticeInfo(final Long noticeId);

    ResponseEntity<NoticeInfoDto> getNoticeInfoByCode(String noticeCode);

    ResponseEntity<MyPage<NoticeInfoDto>> getAllNotice(Pageable pageable);

    ResponseEntity<NoticeInfoDto> modifyById(ModifyNoticeInfoRequestDto requestDto);

    ResponseEntity<NoticeInfoDto> modifyByCode(ModifyNoticeInfoRequestDto requestDto);

    void deleteById(Long noticeId);

    void deleteByCode(String code);


}
