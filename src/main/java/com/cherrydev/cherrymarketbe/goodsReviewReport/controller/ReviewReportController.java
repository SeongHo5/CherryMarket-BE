package com.cherrydev.cherrymarketbe.goodsReviewReport.controller;

import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportRequestDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.service.ReviewReportServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review-report")
public class ReviewReportController {

    private final ReviewReportServiceImpl  reviewReportService;

    // ==================== INSERT ==================== //
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addReviewReport(final @Valid @RequestBody ReviewReportRequestDto reviewReportRequestDto) {
        reviewReportService.save(reviewReportRequestDto);
    }

    // ==================== SELECT ==================== //

    // ==================== DELETE ==================== //
}
