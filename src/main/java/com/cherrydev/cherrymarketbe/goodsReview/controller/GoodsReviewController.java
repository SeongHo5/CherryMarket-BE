package com.cherrydev.cherrymarketbe.goodsReview.controller;

import com.cherrydev.cherrymarketbe.goods.service.GoodsServiceImpl;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import com.cherrydev.cherrymarketbe.goodsReview.service.GoodsReviewServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods-review")
public class GoodsReviewController {
    private final GoodsReviewServiceImpl goodsReviewService;

    // ==================== INSERT ==================== //
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addInquiry(final @Valid @RequestBody GoodsReviewRequestDto goodsReviewRequestDto) {
        goodsReviewService.save(goodsReviewRequestDto);
    }

    // ==================== SELECT ==================== //


    // ==================== UPDATE ==================== //


    // ==================== DELETE ==================== //
}
