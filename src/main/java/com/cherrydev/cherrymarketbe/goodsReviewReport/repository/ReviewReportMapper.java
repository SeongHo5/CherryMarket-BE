package com.cherrydev.cherrymarketbe.goodsReviewReport.repository;

import com.cherrydev.cherrymarketbe.goodsReviewReport.entity.ReviewReport;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewReportMapper {

    //저장
    void save(ReviewReport reviewReport);
}
