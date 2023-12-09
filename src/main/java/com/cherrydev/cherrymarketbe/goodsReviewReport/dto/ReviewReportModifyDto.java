package com.cherrydev.cherrymarketbe.goodsReviewReport.dto;

import lombok.Getter;

@Getter
public class ReviewReportModifyDto {

    Long reportId;
    String answerContent;

    public ReviewReportModifyDto(Long reportId, String answerContent) {
        this.reportId = reportId;
        this.answerContent = answerContent;
    }


}
