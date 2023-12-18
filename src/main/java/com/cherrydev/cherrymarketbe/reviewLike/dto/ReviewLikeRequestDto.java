package com.cherrydev.cherrymarketbe.reviewLike.dto;

import com.cherrydev.cherrymarketbe.reviewLike.entity.ReviewLike;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReviewLikeRequestDto {

    Long reviewId;
    Long userId;

    public ReviewLike toEntity() {
        return ReviewLike.builder()
                .reviewId(reviewId)
                .userId(userId)
                .build();
    }

}
