package com.cherrydev.cherrymarketbe.factory;

import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportModifyDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportRequestDto;

public class GoodsReviewReportFactory {


    //등록 성공
    public static ReviewReportRequestDto createReviewReportRequestDtoA() {
        return ReviewReportRequestDto.builder()
                .reviewId(22L)
                .userId(159L)
                .reportType("FLOODING")
                .content("테스트 합니다.")
                .build();
    }

    //중복 신고
    public static ReviewReportRequestDto createReviewReportRequestDtoF() {
        return ReviewReportRequestDto.builder()
                .reviewId(8L)
                .userId(159L)
                .reportType("FLOODING")
                .content("테스트 합니다.")
                .build();
    }

    //내용 누락
    public static ReviewReportRequestDto createReviewReportRequestDtoB() {
        return ReviewReportRequestDto.builder()
                .reviewId(8L)
                .userId(159L)
                .reportType("FLOODING")
                .content("")
                .build();
    }

    //신고 카테고리 누락
    public static ReviewReportRequestDto createReviewReportRequestDtoC() {
        return ReviewReportRequestDto.builder()
                .reviewId(8L)
                .userId(159L)
                .reportType("")
                .content("테스트 합니다.")
                .build();
    }

    //신고 답변 등록
    public static ReviewReportModifyDto createReviewReportRequestDtoD() {
        return ReviewReportModifyDto.builder()
                .reportId(11L)
                .answerContent("답변 테스트")
                .build();
    }

    //신고 답변 누락
    public static ReviewReportModifyDto createReviewReportRequestDtoE() {
        return ReviewReportModifyDto.builder()
                .reportId(11L)
                .answerContent("")
                .build();
    }



}
