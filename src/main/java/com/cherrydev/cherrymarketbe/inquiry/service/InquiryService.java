package com.cherrydev.cherrymarketbe.inquiry.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryInfoDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.ModifyInquiryRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface InquiryService {
    void createInquiry(final InquiryRequestDto inquiryRequestDto, AccountDetails accountDetails);

    void deleteInquiryById(Long inquiryId, AccountDetails accountDetails);

    void deleteInquiryByCode(String inquiryCode, AccountDetails accountDetails);

    ResponseEntity<InquiryInfoDto> getInquiryInfoById(Long inquiryId);

    ResponseEntity<InquiryInfoDto> getInquiryInfoByCode(String inquiryCode);

    ResponseEntity<MyPage<InquiryInfoDto>> findAll(Pageable pageable);

    ResponseEntity<MyPage<InquiryInfoDto>> findAllByUser(Pageable pageable, Long userId);

    ResponseEntity<MyPage<InquiryInfoDto>> findAllByPhone(Pageable pageable, String phone);

    ResponseEntity<InquiryInfoDto> modifyInquiryById(ModifyInquiryRequestDto requestDto,AccountDetails accountDetails);

    ResponseEntity<InquiryInfoDto> modifyInquiryByCode(ModifyInquiryRequestDto requestDto, AccountDetails accountDetails);


    ResponseEntity<MyPage<InquiryInfoDto>> findAllMyList(Pageable pageable, Long accountId);
}
