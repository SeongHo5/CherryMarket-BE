package com.cherrydev.cherrymarketbe.goodsReview.dto;

import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.cherrydev.cherrymarketbe.common.utils.TimeFormatter.timeStampToString;
import static com.cherrydev.cherrymarketbe.goodsReview.utils.BadWordFilter.CheckForForbiddenWords;

@Getter
@NoArgsConstructor(force = true)
public class GoodsReviewInfoDto {
    Long reviewId;
    Long ordersId;
    Long goodsId;
    Long userId;
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
        this.goodsId = goodsReview.getGoodsId();
        this.userId = goodsReview.getUserId();
        this.code = goodsReview.getCode();
        this.isBest = goodsReview.getIsBest().toString();
        this.subject = goodsReview.getSubject();
        this.content = CheckForForbiddenWords(goodsReview.getContent());
        this.status = goodsReview.getStatus().toString();
        this.createDate = timeStampToString(goodsReview.getCreateDate());
        this.deleteDate = goodsReview.getDeleteDate() != null ? timeStampToString(goodsReview.getDeleteDate()) : null;
    }

    public GoodsReviewInfoDto(GoodsReview goodsReview, Long userId) {
        this.reviewId = goodsReview.getReviewId();
        this.ordersId = goodsReview.getOrdersId();
        this.goodsId = goodsReview.getGoodsId();
        this.userId = userId;
        this.code = goodsReview.getCode();
        this.isBest = goodsReview.getIsBest().toString();
        this.subject = goodsReview.getSubject();
        this.content = CheckForForbiddenWords(goodsReview.getContent());
        this.status = goodsReview.getStatus().toString();
        this.createDate = timeStampToString(goodsReview.getCreateDate());
        this.deleteDate = goodsReview.getDeleteDate() != null ? timeStampToString(goodsReview.getDeleteDate()) : null;
    }

    public void updateContent(String content){
        this.content = content;
    }

    public void updateSubject(String subject){
        this.subject = subject;
    }


}
