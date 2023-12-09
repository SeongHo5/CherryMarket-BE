package com.cherrydev.cherrymarketbe.reviewLike.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewLike {

    private Long likeId;
    private Long reviewId;
    private Long userId;

    @Builder
    public ReviewLike(Long likeId, Long reviewId, Long userId) {
        this.likeId = likeId;
        this.reviewId = reviewId;
        this.userId = userId;
    }
}
