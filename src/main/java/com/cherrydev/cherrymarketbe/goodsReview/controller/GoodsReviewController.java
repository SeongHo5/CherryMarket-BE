package com.cherrydev.cherrymarketbe.goodsReview.controller;

import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewInfoDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewModifyDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import com.cherrydev.cherrymarketbe.goodsReview.service.GoodsReviewServiceImpl;
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
    @GetMapping("/search")
    public ResponseEntity<GoodsReviewInfoDto> getGoodsReview(@RequestParam Long ordersId, @RequestParam Long goodsId) {
        return goodsReviewService.getReview(ordersId, goodsId);
    }

    @GetMapping("/list")
    public ResponseEntity<List<GoodsReviewInfoDto>> getGoodsReviewList() {
        return goodsReviewService.findAll();
    }

    @GetMapping("/list-goods")
    public ResponseEntity<List<GoodsReviewInfoDto>> getGoodsReviewListByGoods(@RequestParam Long goodsId) {
        return goodsReviewService.findAllByGoodsId(goodsId);
    }

    @GetMapping("/list-user")
    public ResponseEntity<List<GoodsReviewInfoDto>> getGoodsReviewListByUser(@RequestParam Long userId) {
        return goodsReviewService.findAllByUser(userId);
    }

    @GetMapping("/list-order")
    public ResponseEntity<List<GoodsReviewInfoDto>> getGoodsReviewListByOrder(@RequestParam Long ordersId) {
        return goodsReviewService.findAllByOrderId(ordersId);
    }


    // ==================== UPDATE ==================== //
    @PatchMapping("/modify")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GoodsReviewInfoDto> modify(
            final @RequestBody GoodsReviewModifyDto modifyDto) {
        return goodsReviewService.update(modifyDto);
    }

    // ==================== DELETE ==================== //
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteInquiryById(@RequestParam Long ordersId, @RequestParam Long goodsId) {
        goodsReviewService.delete(ordersId, goodsId);
    }

}
