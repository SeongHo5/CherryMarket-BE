package com.cherrydev.cherrymarketbe.goodsReview.service;

import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewInfoDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import org.springframework.http.ResponseEntity;

public interface GoodsReviewService {
    //저장
    void save(final GoodsReviewRequestDto goodsReviewRequestDto);
    //업데이트
    void update();
    //삭제
    void delete();
    //조회
    ResponseEntity<GoodsReviewInfoDto> getReview(final Long ordersId, final Long goodsId);
    //전체조회
    void getReviewList();
    //전체조회 - 상품별
    void getGoodsReviewListByGoodsId();
    // 전체조회 - 좋아요수
    void getGoodsReviewListByUserId();
}
