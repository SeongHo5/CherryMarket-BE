package com.cherrydev.cherrymarketbe.goodsReviewReport.dto;

import com.cherrydev.cherrymarketbe.goodsReviewReport.entity.ReviewReport;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.cherrydev.cherrymarketbe.common.utils.TimeFormatter.timeStampToString;

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
        this.content = reviewReport.getContent();
        this.answerContent = reviewReport.getAnswerContent();
        this.answerStatus = reviewReport.getAnswerStatus().toString();
        this.createDate = reviewReport.getCreateDate() != null ? timeStampToString(reviewReport.getCreateDate()) : null;
    }

    public static List<ReviewReportInfoDto> convertToDtoList(List<ReviewReport> reviewReports) {
        return reviewReports.stream()
                .map(ReviewReportInfoDto::new)
                .toList();
    }
}
