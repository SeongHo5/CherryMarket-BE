package com.cherrydev.cherrymarketbe.goodsReview.repository;

import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsReviewMapper {
    //저장
    void save(GoodsReview goodsReview);

    //업데이트
    void update(GoodsReview goodsReview);

    //삭제
    void delete(Long ordersId, Long goodsId);

    //조회
    GoodsReview findReivew(Long ordersId, Long goodsId);

    //전체조회
    List<GoodsReview> findAll();

    //전체조회 - 상품별
    List<GoodsReview> findAllByGoodsId(Long goodsId);

    // 전체조회 - 주문별
    List<GoodsReview> findAllByOrderId(Long ordersId);

    // 전체조회 - 유저
    List<GoodsReview> findAllByUserId(Long userId);

    boolean existReview(GoodsReview goodsReview);

    boolean checkDeliveryStatus(GoodsReview goodsReview);


}
