package com.cherrydev.cherrymarketbe.goodsReview.controller;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.common.service.FileService;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewInfoDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewModifyDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import com.cherrydev.cherrymarketbe.goodsReview.service.GoodsReviewServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods-review")
public class GoodsReviewController {

    private final GoodsReviewServiceImpl goodsReviewService;
    private final FileService fileService;

    // ==================== INSERT ==================== //
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addReivew(final @Valid @RequestBody GoodsReviewRequestDto goodsReviewRequestDto) {
        goodsReviewService.save(goodsReviewRequestDto);
    }

    @PostMapping("/add-image")
    @ResponseStatus(HttpStatus.CREATED)
    public void addImage(@RequestPart("imageFiles") List<MultipartFile> imageFiles, String dirName) {
        fileService.uploadMultipleFiles(imageFiles, dirName);
    }


    // ==================== SELECT ==================== //
    @GetMapping("/search")
    public ResponseEntity<GoodsReviewInfoDto> getGoodsReview(@RequestParam Long ordersId, @RequestParam Long goodsId) {
        return goodsReviewService.getReview(ordersId, goodsId);
    }

    @GetMapping("/list")
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> getGoodsReviewList(final Pageable pageable) {
        return goodsReviewService.findAll(pageable);
    }

    @GetMapping("/list-goods")
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> getGoodsReviewListByGoods(final Pageable pageable, @RequestParam Long goodsId) {
        return goodsReviewService.findAllByGoodsId(pageable, goodsId);
    }

    @GetMapping("/list-user")
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> getGoodsReviewListByUser(final Pageable pageable, @RequestParam Long userId) {
        return goodsReviewService.findAllByUser(pageable, userId);
    }

    @GetMapping("/list-order")
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> getGoodsReviewListByOrder(final Pageable pageable, @RequestParam Long ordersId) {
        return goodsReviewService.findAllByOrderId(pageable, ordersId);
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
    public void deleteGoodsReviewById(@RequestParam Long ordersId, @RequestParam Long goodsId) {
        goodsReviewService.delete(ordersId, goodsId);
    }

}
