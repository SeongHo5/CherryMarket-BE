package com.cherrydev.cherrymarketbe.goodsReviewReport.dto;

import com.cherrydev.cherrymarketbe.goodsReviewReport.entity.ReviewReport;
import com.cherrydev.cherrymarketbe.goodsReviewReport.enums.ReviewReportAnswer;
import com.cherrydev.cherrymarketbe.goodsReviewReport.enums.ReviewReportType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReviewReportRequestDto {

    Long reviewId;
    Long userId;
    String reportType;
    String content;
    String answerStatus;
    String answerContent;

    public ReviewReport toEntity() {
        return ReviewReport.builder()
                .reviewId(this.getReviewId())
                .userId(this.getUserId())
                .reportType(ReviewReportType.valueOf(this.getReportType()))
                .content(this.getContent())
                .answerStatus(ReviewReportAnswer.NOT_EXIST)
                .answerContent("")
                .build();
    }
}
