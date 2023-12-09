package com.cherrydev.cherrymarketbe.reviewLike.controller;

import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewInfoDto;
import com.cherrydev.cherrymarketbe.reviewLike.dto.ReviewLikeCountDto;
import com.cherrydev.cherrymarketbe.reviewLike.dto.ReviewLikeRequestDto;
import com.cherrydev.cherrymarketbe.reviewLike.service.ReviewLikeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review-like")
public class ReviewLikeController {

    private final ReviewLikeServiceImpl reviewLikeService;

    // ==================== INSERT ==================== //

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addReivewLike(final @Valid @RequestBody ReviewLikeRequestDto reviewLikeDto) {
        reviewLikeService.save(reviewLikeDto);
    }

    // ==================== SELECT ==================== //
    //리뷰Id에 대한 좋아요 수 카운트
//    @GetMapping("/count-like")
//    public ResponseEntity<Long> getReviewLike(@RequestParam Long reviewId) {
//        return reviewLikeService.countLike(reviewId);
//    }

    @GetMapping("/count-like")
    public ResponseEntity<ReviewLikeCountDto> getReviewLike(@RequestParam Long reviewId) {
        return reviewLikeService.countLike(reviewId);
    }


    // ==================== UPDATE ==================== //


    // ==================== DELETE ==================== //
}
