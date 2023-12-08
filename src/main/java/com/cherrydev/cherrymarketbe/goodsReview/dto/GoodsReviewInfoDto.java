package com.cherrydev.cherrymarketbe.goodsReview.dto;

import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;
import lombok.Getter;

import java.util.List;

import static com.cherrydev.cherrymarketbe.common.utils.TimeFormatter.timeStampToString;

@Getter
public class GoodsReviewInfoDto {
    Long reviewId;
    Long ordersId;
    String code;
    String isBest;
    String subject;
    String content;
    String status;
    String createDate;
    String deleteDate;

    public GoodsReviewInfoDto(GoodsReview goodsReview) {
        this.reviewId = goodsReview.getReviewId();
        this.ordersId = goodsReview.getOrdersId();
        this.code = goodsReview.getCode();
        this.isBest = goodsReview.getIsBest().toString();
        this.subject = goodsReview.getSubject();
        this.content = goodsReview.getContent();
        this.status = goodsReview.getStatus().toString();
        this.createDate = timeStampToString(goodsReview.getCreateDate());
        this.deleteDate = goodsReview.getDeleteDate() != null ? timeStampToString(goodsReview.getDeleteDate()) : null;
    }

    public static List<GoodsReviewInfoDto> convertToDtoList(List<GoodsReview> goodsReviews) {
        return goodsReviews.stream()
                .map(GoodsReviewInfoDto::new)
                .toList();
    }

}
