package com.cherrydev.cherrymarketbe.goodsReviewReport.service;


import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportRequestDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.repository.ReviewReportMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewReportServiceImpl implements ReviewReportService {

    private final ReviewReportMapper reviewReportMapper;

    @Override
    @Transactional
    public void save(ReviewReportRequestDto reviewReportRequestDto) {
        reviewReportMapper.save(reviewReportRequestDto.toEntity());
    }
}
