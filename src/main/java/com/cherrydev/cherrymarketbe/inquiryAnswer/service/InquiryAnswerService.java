package com.cherrydev.cherrymarketbe.inquiryAnswer.service;

import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnswerInfoDto;
import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnwerRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface InquiryAnswerService {

    void createInquiryAnswer(final InquiryAnwerRequestDto inquiryAnwerRequestDto);

    void updateInquirStatus(final InquiryAnwerRequestDto inquiryAnwerRequestDto);

    ResponseEntity<InquiryAnswerInfoDto> getInquiryAnswerById(Long inquiryId);

    ResponseEntity<List<InquiryAnswerInfoDto>> getAnswerByUserId(Long userId);

    ResponseEntity<List<InquiryAnswerInfoDto>> getAnswerByEmail(String email);

    ResponseEntity<List<InquiryAnswerInfoDto>> findAll();

    void deleteAndSetStatusAccepting(Long answerId);
}
