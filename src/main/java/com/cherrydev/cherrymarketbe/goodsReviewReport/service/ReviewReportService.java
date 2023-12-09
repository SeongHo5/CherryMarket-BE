package com.cherrydev.cherrymarketbe.goodsReviewReport.service;

import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportRequestDto;

public interface ReviewReportService {

    //저장
    void save(final ReviewReportRequestDto reviewReportRequestDto);


}
