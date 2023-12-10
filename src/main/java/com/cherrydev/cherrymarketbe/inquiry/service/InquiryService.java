package com.cherrydev.cherrymarketbe.inquiry.service;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.ModifyInquiryRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface InquiryService {
    void createInquiry(final InquiryRequestDto inquiryRequestDto);

    void deleteInquiryById(Long inquiryId);

    void deleteInquiryByCode(String inquiryCode);

    ResponseEntity<InquiryInfoDto> getInquiryInfoById(Long inquiryId);

    ResponseEntity<InquiryInfoDto> getInquiryInfoByCode(String inquiryCode);

    ResponseEntity<MyPage<InquiryInfoDto>> findAll(Pageable pageable);

    ResponseEntity<MyPage<InquiryInfoDto>> findAllByUser(Pageable pageable, Long userId);

    ResponseEntity<MyPage<InquiryInfoDto>> findAllByPhone(Pageable pageable, String phone);

    ResponseEntity<InquiryInfoDto> modifyInquiryById(ModifyInquiryRequestDto requestDto);

    ResponseEntity<InquiryInfoDto> modifyInquiryByCode(ModifyInquiryRequestDto requestDto);


}
