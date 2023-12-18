package com.cherrydev.cherrymarketbe.factory;

import com.cherrydev.cherrymarketbe.inquiryAnswer.dto.InquiryAnwerRequestDto;

public class InquiryAnswerFactory {


    //등록 성공
    public static InquiryAnwerRequestDto createInquiryAnswerA() {
        return InquiryAnwerRequestDto.builder()
                .inquiryId(279L)
                .memo("문의 답변 테스트")
                .content("테스트 성공")
                .build();
    }

    //내용 누락
    public static InquiryAnwerRequestDto createInquiryAnswerB() {
        return InquiryAnwerRequestDto.builder()
                .inquiryId(277L)
                .memo("")
                .content("")
                .build();
    }

    //중복
    public static InquiryAnwerRequestDto createInquiryAnswerC() {
        return InquiryAnwerRequestDto.builder()
                .inquiryId(276L)
                .memo("문의 답변 중복 테스트")
                .content("테스트 성공")
                .build();
    }


}
