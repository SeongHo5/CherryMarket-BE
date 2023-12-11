package com.cherrydev.cherrymarketbe.goodsReviewReport.dto;

import com.cherrydev.cherrymarketbe.goodsReviewReport.entity.ReviewReport;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.cherrydev.cherrymarketbe.common.utils.TimeFormatter.timeStampToString;
import static com.cherrydev.cherrymarketbe.goodsReview.utils.BadWordFilter.CheckForForbiddenWords;

@Getter
@NoArgsConstructor(force = true)
public class ReviewReportInfoDto {

    Long reportId;
    Long reviewId;
    Long userId;
    String reportType;
    String content;
    String answerContent;
    String answerStatus;
    String createDate;

    public ReviewReportInfoDto(ReviewReport reviewReport) {
        this.reportId = reviewReport.getReportId();
        this.reviewId = reviewReport.getReviewId();
        this.userId = reviewReport.getUserId();
        this.reportType = reviewReport.getReportType().toString();
        this.content = CheckForForbiddenWords(reviewReport.getContent());
        this.answerContent = reviewReport.getAnswerContent();
        this.answerStatus = reviewReport.getAnswerStatus().toString();
        this.createDate = reviewReport.getCreateDate() != null ? timeStampToString(reviewReport.getCreateDate()) : null;
    }

    public void updateContent(String content){
        this.content = content;
    }

}
