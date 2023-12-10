package com.cherrydev.cherrymarketbe.goodsReviewReport.repository;

import com.cherrydev.cherrymarketbe.goodsReviewReport.dto.ReviewReportInfoDto;
import com.cherrydev.cherrymarketbe.goodsReviewReport.entity.ReviewReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewReportMapper {

    //저장
    void save(ReviewReport reviewReport);

    //수정
    void update(ReviewReport reviewReport);

    //삭제(완료시 사용)
    void delete(ReviewReport reviewReport);

    //조회
    ReviewReport findReport(Long reportId);

    //전체 조회
    List<ReviewReportInfoDto> findAll();

    //전체 조회 - 답변 여부로 확인
    List<ReviewReportInfoDto> findAllByStatus();

    //중복신고 검사
    boolean checkDupulicateReport(ReviewReport reviewReport);



}
