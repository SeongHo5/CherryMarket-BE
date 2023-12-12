package com.cherrydev.cherrymarketbe.goodsReviewReport.service;


import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.goodsReview.utils.BadWordFilter;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportInfoDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportModifyDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportRequestDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.entity.ReviewReport;
import com.cherrydev.cherrymarketbe.goodsReviewReport.enums.ReviewReportAnswer;
import com.cherrydev.cherrymarketbe.goodsReviewReport.repository.ReviewReportMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.PAGE_HEADER;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.createPage;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewReportServiceImpl implements ReviewReportService {

    private final ReviewReportMapper reviewReportMapper;

    @Override
    @Transactional
    public void save(ReviewReportRequestDto reviewReportRequestDto) {
        CheckValidationForInsert(reviewReportRequestDto);
        reviewReportMapper.save(reviewReportRequestDto.toEntity());
    }

    @Override
    @Transactional
    public ResponseEntity<ReviewReportInfoDto> update(ReviewReportModifyDto modifyDto) {
        CheckValidationForModify(modifyDto);
        CheckDupulicateAnswer(modifyDto);
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
    public ResponseEntity<MyPage<ReviewReportInfoDto>> findAll(final Pageable pageable) {

        List<ReviewReportInfoDto> getDto = reviewReportMapper.findAll();
        getDto.forEach(dto -> dto.updateContent(CheckForForbiddenWordsTest(dto.getContent())));
        MyPage<ReviewReportInfoDto> infoPage = createPage(pageable, () -> getDto);

        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<MyPage<ReviewReportInfoDto>> findAllByStatus(final Pageable pageable) {

        List<ReviewReportInfoDto> getDto = reviewReportMapper.findAllByStatus();
        getDto.forEach(dto -> dto.updateContent(CheckForForbiddenWordsTest(dto.getContent())));
        MyPage<ReviewReportInfoDto> infoPage = createPage(pageable, () -> getDto);

        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    // =============== PRIVATE METHODS =============== //
    private void CheckDupulicateReport(ReviewReport reviewReport) {
        if (reviewReportMapper.checkDupulicateReport(reviewReport)) {
            throw new DuplicatedException(ALREADY_EXIST_REPORT);
        }
    }

    private void CheckDupulicateAnswer(ReviewReportModifyDto reviewReportModifyDto) {
        if (reviewReportMapper.findAnswer(reviewReportModifyDto.getReportId()) != null ||  reviewReportModifyDto.getAnswerContent().equals("")){
            throw new DuplicatedException(ALREADY_EXIST_ANSWER);
        }
    }

    private String CheckForForbiddenWordsTest(String content) {
        System.out.println("CheckForForbiddenWords start");
        return BadWordFilter.filterAndReplace(content);
    }

    private void CheckValidationForInsert(ReviewReportRequestDto reviewReportRequestDto) {
        if (reviewReportRequestDto.getContent() == null || reviewReportRequestDto.getContent().equals("")) {
            throw new ServiceFailedException(NOT_ALLOWED_EMPTY_CONTENT);
        }
        if (reviewReportRequestDto.getReportType() == null || reviewReportRequestDto.getReportType().equals("")) {
            throw new ServiceFailedException(NOT_ALLOWED_EMPTY_CATEGORY);
        }
        CheckDupulicateReport(reviewReportRequestDto.toEntity());

    }

    private void CheckValidationForModify(ReviewReportModifyDto modifydto) {
        if (modifydto.getAnswerContent() == null || modifydto.getAnswerContent().equals("")) {
            throw new ServiceFailedException(NOT_ALLOWED_EMPTY_CONTENT);
        }
    }
}
