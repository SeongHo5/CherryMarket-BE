package com.cherrydev.cherrymarketbe.goodsReview.service;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewInfoDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewModifyDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

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
    ResponseEntity<MyPage<GoodsReviewInfoDto>> findAll(final Pageable pageable);

    //전체조회 - 상품별
    ResponseEntity<MyPage<GoodsReviewInfoDto>> findAllByGoodsId(final Pageable pageable, final Long goodsId);

    // 전체조회 - 주문
    ResponseEntity<MyPage<GoodsReviewInfoDto>> findAllByOrderId(final Pageable pageable, final Long ordersId);

    // 전체조회 - 유저
    ResponseEntity<MyPage<GoodsReviewInfoDto>> findAllByUser(final Pageable pageable, Long userId);


}
