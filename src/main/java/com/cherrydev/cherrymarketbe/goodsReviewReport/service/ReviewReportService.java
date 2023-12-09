package com.cherrydev.cherrymarketbe.goodsReviewReport.service;

import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportInfoDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportModifyDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReviewReportService {

    //저장
    void save(final ReviewReportRequestDto reviewReportRequestDto);

    ResponseEntity<ReviewReportInfoDto> update(ReviewReportModifyDto modifyDto);

    ResponseEntity<ReviewReportInfoDto> findReport(Long reportId);

    ResponseEntity<List<ReviewReportInfoDto>> findAll();

    ResponseEntity<List<ReviewReportInfoDto>> findAllByStatus();
}
