package com.cherrydev.cherrymarketbe.goodsReviewReport.entity;

import com.cherrydev.cherrymarketbe.goodsReviewReport.enums.ReviewReportAnswer;
import com.cherrydev.cherrymarketbe.goodsReviewReport.enums.ReviewReportType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class ReviewReport {

    private Long reportId;
    private Long reviewId;
    private Long userId;
    private ReviewReportType reportType;
    private String content;
    private String answerContent;
    private ReviewReportAnswer answerStatus;
    private Timestamp createDate;

    @Builder
    public ReviewReport(Long reportId, Long reviewId, Long userId,
                        ReviewReportType reportType, String content,
                        String answerContent, ReviewReportAnswer answerStatus,
                        Timestamp createDate) {

        this.reportId = reportId;
        this.reviewId = reviewId;
        this.userId = userId;
        this.reportType = reportType;
        this.content = content;
        this.answerContent = answerContent;
        this.answerStatus = answerStatus;
        this.createDate = createDate;
    }

}
