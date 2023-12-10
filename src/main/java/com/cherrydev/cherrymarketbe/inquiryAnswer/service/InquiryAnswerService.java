package com.cherrydev.cherrymarketbe.inquiryAnswer.service;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnswerInfoDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnwerRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface InquiryAnswerService {

    void createInquiryAnswer(final InquiryAnwerRequestDto inquiryAnwerRequestDto);

    void updateInquirStatus(final InquiryAnwerRequestDto inquiryAnwerRequestDto);

    ResponseEntity<InquiryAnswerInfoDto> getInquiryAnswerById(Long inquiryId);

    ResponseEntity<MyPage<InquiryAnswerInfoDto>> getAnswerByUserId(Pageable pageable, Long userId);

    ResponseEntity<MyPage<InquiryAnswerInfoDto>> getAnswerByEmail(Pageable pageable, String email);

    ResponseEntity<MyPage<InquiryAnswerInfoDto>> findAll(Pageable pageable);

    void deleteAndSetStatusAccepting(Long answerId);
}
