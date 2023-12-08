package com.cherrydev.cherrymarketbe.goodsReview.service;

import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewInfoDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewModifyDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GoodsReviewService {
    //저장
    void save(final GoodsReviewRequestDto goodsReviewRequestDto);

    //업데이트
    ResponseEntity<GoodsReviewInfoDto> update(GoodsReviewModifyDto modifyDto);

    //삭제
    void delete(final Long ordersId, Long goodsId);

    //조회
    ResponseEntity<GoodsReviewInfoDto> getReview(final Long ordersId, final Long goodsId);

    //전체조회
    ResponseEntity<List<GoodsReviewInfoDto>> findAll();

    //전체조회 - 상품별
    ResponseEntity<List<GoodsReviewInfoDto>> findAllByGoodsId(final Long goodsId);

    // 전체조회 - 주문
    ResponseEntity<List<GoodsReviewInfoDto>> findAllByOrderId(final Long ordersId);

    // 전체조회 - 유저
    ResponseEntity<List<GoodsReviewInfoDto>> findAllByUser(Long userId);

    // 전체조회 - 좋아요 수
    void findAllByLike();


}
