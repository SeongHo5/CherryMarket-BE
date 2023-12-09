package com.cherrydev.cherrymarketbe.reviewLike.dto;

import lombok.Getter;

@Getter
public class ReviewLikeCountDto {

    Long count;

    public ReviewLikeCountDto(Long count) {
        this.count = count;
    }
}
