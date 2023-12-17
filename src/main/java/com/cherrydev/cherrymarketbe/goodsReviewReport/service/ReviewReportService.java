package com.cherrydev.cherrymarketbe.goodsReviewReport.service;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportInfoDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportModifyDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ReviewReportService {

    //저장
    void save(final ReviewReportRequestDto reviewReportRequestDto);

    ResponseEntity<ReviewReportInfoDto> update(ReviewReportModifyDto modifyDto);

    ResponseEntity<ReviewReportInfoDto> findReport(Long reportId);

    ResponseEntity<MyPage<ReviewReportInfoDto>> findAll(final Pageable pageable);

    ResponseEntity<MyPage<ReviewReportInfoDto>> findAllByStatus(final Pageable pageable);
}
