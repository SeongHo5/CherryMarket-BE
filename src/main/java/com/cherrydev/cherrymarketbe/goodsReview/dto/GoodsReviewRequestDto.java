package com.cherrydev.cherrymarketbe.goodsReview.dto;

import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;
import com.cherrydev.cherrymarketbe.goodsReview.enums.GoodsReviewBest;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GoodsReviewRequestDto {

    Long ordersId;
    String code;
    String isBest;
    String subject;
    String content;
    String status;

    public GoodsReview toEntity() {
        return GoodsReview.builder()
                .ordersId(this.getOrdersId())
                .code("1")
//                .isBest(GoodsReviewBest.valueOf(this.getIsBest()))
                .isBest(GoodsReviewBest.NORMAL)
                .subject(this.getSubject())
                .content(this.getContent())
                .status(DisplayStatus.ACTIVE)
                .build();
    }
}
