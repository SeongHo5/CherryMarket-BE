package com.cherrydev.cherrymarketbe.inquiry.service;

import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import org.springframework.http.ResponseEntity;


public interface InquiryService {
    void createInquiry(final InquiryRequestDto inquiryRequestDto);

    void deleteInquiryById(Long inquiryId);

    ResponseEntity<InquiryInfoDto> getInquiryInfoById(Long inquiryId);

    ResponseEntity<InquiryInfoDto> getInquiryInfoByCode(String inquiryCode);

//
//    ResponseEntity<NoticeInfoDto> getNoticeInfo(final Long noticeId);
//    ResponseEntity<NoticeInfoDto> getNoticeInfoByCode(String noticeCode);
//
//    ResponseEntity<List<NoticeInfoDto>> findAll();
//
//    ResponseEntity<NoticeInfoDto> modifyNotice(ModifyNoticeInfoRequestDto requestDto, final Long noticeId);
//    void deleteNotice(Long noticeId);


}
