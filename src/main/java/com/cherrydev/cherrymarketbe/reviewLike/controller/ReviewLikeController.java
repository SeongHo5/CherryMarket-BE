package com.cherrydev.cherrymarketbe.reviewLike.controller;

import com.cherrydev.cherrymarketbe.reviewLike.dto.ReviewLikeCountDto;
import com.cherrydev.cherrymarketbe.reviewLike.dto.ReviewLikeRequestDto;
import com.cherrydev.cherrymarketbe.reviewLike.service.ReviewLikeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/count-like")
    public ResponseEntity<ReviewLikeCountDto> getReviewLike(@RequestParam Long reviewId) {
        return reviewLikeService.countLike(reviewId);
    }

}
