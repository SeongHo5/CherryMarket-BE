package com.cherrydev.cherrymarketbe.inquiry.service;

import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.ModifyInquiryRequestDto;
import com.cherrydev.cherrymarketbe.notice.dto.ModifyNoticeInfoRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface InquiryService {
    void createInquiry(final InquiryRequestDto inquiryRequestDto);

    void deleteInquiryById(Long inquiryId);

    ResponseEntity<InquiryInfoDto> getInquiryInfoById(Long inquiryId);

    ResponseEntity<InquiryInfoDto> getInquiryInfoByCode(String inquiryCode);

    ResponseEntity<List<InquiryInfoDto>> findAll();

    ResponseEntity<InquiryInfoDto> modifyInquiry(ModifyInquiryRequestDto requestDto);


}
