package com.cherrydev.cherrymarketbe.inquiry.service;

import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.ModifyInquiryRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface InquiryService {
    void createInquiry(final InquiryRequestDto inquiryRequestDto);

    void deleteInquiryById(Long inquiryId);

    void deleteInquiryByCode(String inquiryCode);

    @Transactional(readOnly = true)
    ResponseEntity<InquiryInfoDto> getInquiryInfoById(Long inquiryId);

    @Transactional(readOnly = true)
    ResponseEntity<InquiryInfoDto> getInquiryInfoByCode(String inquiryCode);

    @Transactional(readOnly = true)
    ResponseEntity<List<InquiryInfoDto>> findAll();

    @Transactional(readOnly = true)
    ResponseEntity<List<InquiryInfoDto>> findAllByUser(Long userId);

    @Transactional(readOnly = true)
    ResponseEntity<List<InquiryInfoDto>> findAllByPhone(String phone);

    ResponseEntity<InquiryInfoDto> modifyInquiryById(ModifyInquiryRequestDto requestDto);

    ResponseEntity<InquiryInfoDto> modifyInquiryByCode(ModifyInquiryRequestDto requestDto);



}
