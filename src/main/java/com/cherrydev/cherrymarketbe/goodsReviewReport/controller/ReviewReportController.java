package com.cherrydev.cherrymarketbe.goodsReviewReport.controller;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportInfoDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportModifyDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportRequestDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.service.ReviewReportServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review-report")
public class ReviewReportController {

    private final ReviewReportServiceImpl reviewReportService;

    // ==================== INSERT ==================== //
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addReviewReport(final @Valid @RequestBody ReviewReportRequestDto reviewReportRequestDto) {
        reviewReportService.save(reviewReportRequestDto);
    }

    // ==================== SELECT ==================== //
    @GetMapping("/search")
    public ResponseEntity<ReviewReportInfoDto> getReviewReport(@RequestParam Long reportId) {
        return reviewReportService.findReport(reportId);
    }

    @GetMapping("/list")
    public ResponseEntity<MyPage<ReviewReportInfoDto>> getReviewReportList(final Pageable pageable) {
        return reviewReportService.findAll(pageable);
    }

    @GetMapping("/list-status")
    public ResponseEntity<MyPage<ReviewReportInfoDto>> getReviewReportListByStatus(final Pageable pageable) {
        return reviewReportService.findAllByStatus(pageable);
    }

    // ==================== UPDATE ==================== //
    @PatchMapping("/add-answer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReviewReportInfoDto> modify(
            final @RequestBody ReviewReportModifyDto modifyDto) {
        return reviewReportService.update(modifyDto);
    }
}
