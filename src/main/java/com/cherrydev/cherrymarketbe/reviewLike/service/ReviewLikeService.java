package com.cherrydev.cherrymarketbe.reviewLike.service;

import com.cherrydev.cherrymarketbe.reviewLike.dto.ReviewLikeCountDto;
import com.cherrydev.cherrymarketbe.reviewLike.dto.ReviewLikeRequestDto;
import org.springframework.http.ResponseEntity;

public interface ReviewLikeService {
    void save(ReviewLikeRequestDto reviewLikeDto);

    ResponseEntity<ReviewLikeCountDto> countLike(Long reviewId);
}
