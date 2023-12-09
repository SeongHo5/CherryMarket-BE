package com.cherrydev.cherrymarketbe.goodsReviewReport.service;


import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportInfoDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportModifyDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportRequestDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.entity.ReviewReport;
import com.cherrydev.cherrymarketbe.goodsReviewReport.enums.ReviewReportAnswer;
import com.cherrydev.cherrymarketbe.goodsReviewReport.repository.ReviewReportMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.ALREADY_EXIST_REPORT;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewReportServiceImpl implements ReviewReportService {

    private final ReviewReportMapper reviewReportMapper;

    @Override
    @Transactional
    public void save(ReviewReportRequestDto reviewReportRequestDto) {
        CheckDupulicateReport(reviewReportRequestDto.toEntity());
        reviewReportMapper.save(reviewReportRequestDto.toEntity());
    }

    @Override
    @Transactional
    public ResponseEntity<ReviewReportInfoDto> update(ReviewReportModifyDto modifyDto) {

        ReviewReport addAnswer = ReviewReport.builder()
                .reportId(modifyDto.getReportId())
                .answerContent(modifyDto.getAnswerContent())
                .answerStatus(ReviewReportAnswer.ANSWERD)
                .build();

        reviewReportMapper.update(addAnswer);
        ReviewReportInfoDto resultDto = new ReviewReportInfoDto(reviewReportMapper.findReport(modifyDto.getReportId()));

        return ResponseEntity
                .ok()
                .body(resultDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ReviewReportInfoDto> findReport(Long reportId) {
        ReviewReport reviewReport = reviewReportMapper.findReport(reportId);
        return ResponseEntity.ok()
                .body(new ReviewReportInfoDto(reviewReport));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<ReviewReportInfoDto>> findAll() {
        List<ReviewReport> reviewReports = reviewReportMapper.findAll();
        List<ReviewReportInfoDto> reviewReportInfoDtos = ReviewReportInfoDto.convertToDtoList(reviewReports);
        return ResponseEntity.ok()
                .body(reviewReportInfoDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<ReviewReportInfoDto>> findAllByStatus() {
        List<ReviewReport> reviewReports = reviewReportMapper.findAllByStatus();
        List<ReviewReportInfoDto> reviewReportInfoDtos = ReviewReportInfoDto.convertToDtoList(reviewReports);
        return ResponseEntity.ok()
                .body(reviewReportInfoDtos);
    }

    // =============== PRIVATE METHODS =============== //
    private void CheckDupulicateReport(ReviewReport reviewReport) {
        if (reviewReportMapper.checkDupulicateReport(reviewReport)) {
            throw new DuplicatedException(ALREADY_EXIST_REPORT);
        }
    }

}
