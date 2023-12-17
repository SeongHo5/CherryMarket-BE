package com.cherrydev.cherrymarketbe.goodsReview.enums;

import lombok.Getter;

@Getter
public enum GoodsReviewBest {
    BEST("베스트리뷰"), NORMAL("일반리뷰"), ;

    private final String BestType;

    GoodsReviewBest(String BestType) {this.BestType = BestType;}

}
