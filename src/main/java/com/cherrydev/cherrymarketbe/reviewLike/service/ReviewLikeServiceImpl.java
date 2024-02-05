package com.cherrydev.cherrymarketbe.reviewLike.service;

import com.cherrydev.cherrymarketbe.reviewLike.dto.ReviewLikeCountDto;
import com.cherrydev.cherrymarketbe.reviewLike.dto.ReviewLikeRequestDto;
import com.cherrydev.cherrymarketbe.reviewLike.entity.ReviewLike;
import com.cherrydev.cherrymarketbe.reviewLike.repository.ReviewLikeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewLikeServiceImpl implements ReviewLikeService {

    private final ReviewLikeMapper reviewLikeMapper;

    @Override
    @Transactional
    public void save(ReviewLikeRequestDto reviewLikeRequestDtoDto) {
        if (!CheckExistLike(reviewLikeRequestDtoDto.toEntity())) {
            reviewLikeMapper.save(reviewLikeRequestDtoDto.toEntity());
        } else {
            reviewLikeMapper.delete(reviewLikeRequestDtoDto.toEntity());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ReviewLikeCountDto> countLike(Long reviewId) {
        Long count = reviewLikeMapper.countLike(reviewId);
        ReviewLikeCountDto likeCount = new ReviewLikeCountDto(count);
        return ResponseEntity.ok()
                .body(likeCount);
    }

    // =============== PRIVATE METHODS =============== //
    private boolean CheckExistLike(ReviewLike reviewLike) {
        return reviewLikeMapper.existLike(reviewLike);
    }
}
