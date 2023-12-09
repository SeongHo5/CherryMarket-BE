package com.cherrydev.cherrymarketbe.goodsReviewReport.controller;

import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportInfoDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportModifyDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportRequestDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.service.ReviewReportServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<ReviewReportInfoDto>> getReviewReportList() {
        return reviewReportService.findAll();
    }

    @GetMapping("/list-status")
    public ResponseEntity<List<ReviewReportInfoDto>> getReviewReportListByStatus() {
        return reviewReportService.findAllByStatus();
    }

    // ==================== UPDATE ==================== //
    @PatchMapping("/add-answer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReviewReportInfoDto> modify(
            final @RequestBody ReviewReportModifyDto modifyDto) {
        return reviewReportService.update(modifyDto);
    }
}
