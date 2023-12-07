package com.cherrydev.cherrymarketbe.inquiryAnswer.service;

import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnwerRequestDto;


public interface InquiryAnswerService {

    void createInquiryAnswer(final InquiryAnwerRequestDto inquiryAnwerRequestDto);

    void updateInquirStatus(final InquiryAnwerRequestDto inquiryAnwerRequestDto);
}
