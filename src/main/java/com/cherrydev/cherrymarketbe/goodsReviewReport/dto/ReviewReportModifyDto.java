package com.cherrydev.cherrymarketbe.goodsReviewReport.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewReportModifyDto {

    Long reportId;
    String answerContent;

    @Builder
    public ReviewReportModifyDto(Long reportId, String answerContent) {
        this.reportId = reportId;
        this.answerContent = answerContent;
    }


}
