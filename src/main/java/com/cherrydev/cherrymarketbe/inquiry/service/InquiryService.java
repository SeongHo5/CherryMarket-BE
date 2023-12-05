package com.cherrydev.cherrymarketbe.inquiry.service;

import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;


public interface InquiryService {
    void createInquiry(final InquiryRequestDto inquiryRequestDto);
//
//    ResponseEntity<NoticeInfoDto> getNoticeInfo(final Long noticeId);
//    ResponseEntity<NoticeInfoDto> getNoticeInfoByCode(String noticeCode);
//
//    ResponseEntity<List<NoticeInfoDto>> findAll();
//
//    ResponseEntity<NoticeInfoDto> modifyNotice(ModifyNoticeInfoRequestDto requestDto, final Long noticeId);
//    void deleteNotice(Long noticeId);


}
