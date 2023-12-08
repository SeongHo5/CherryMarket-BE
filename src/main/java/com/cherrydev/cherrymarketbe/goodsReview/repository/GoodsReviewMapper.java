package com.cherrydev.cherrymarketbe.goodsReview.repository;

import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;

public interface GoodsReviewMapper {
    //저장
    void save(GoodsReview goodsReview);
    //업데이트
    void update();
    //삭제
    void delete();
    //조회
    void getReview();
    //전체조회
    void getReviewList();
    //전체조회 - 상품별
    void getGoodsReviewListByGoodsId();
    // 전체조회 - 좋아요수
    void getGoodsReviewListByUserId();
}
