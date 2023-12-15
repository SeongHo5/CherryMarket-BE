package com.cherrydev.cherrymarketbe.goodsReview.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public void addReivew(final @Valid @RequestBody GoodsReviewRequestDto goodsReviewRequestDto, final @AuthenticationPrincipal AccountDetails accountDetails) {
        goodsReviewService.save(goodsReviewRequestDto, accountDetails);
    }

    @PostMapping("/add-image")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public void addImage(@RequestPart("imageFiles") List<MultipartFile> imageFiles, String dirName) {
        fileService.uploadMultipleFiles(imageFiles, dirName);
    }



    // ==================== SELECT ==================== //
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<GoodsReviewInfoDto> getGoodsReview(@RequestParam Long ordersId, @RequestParam Long goodsId) {
        return goodsReviewService.getReview(ordersId, goodsId);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> getGoodsReviewList(final Pageable pageable) {
        return goodsReviewService.findAll(pageable);
    }

    @GetMapping("/list-goods")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> getGoodsReviewListByGoods(final Pageable pageable, @RequestParam Long goodsId) {
        return goodsReviewService.findAllByGoodsId(pageable, goodsId);
    }

    @GetMapping("/list-user")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> getGoodsReviewListByUser(final Pageable pageable, @RequestParam Long userId) {
        return goodsReviewService.findAllByUser(pageable, userId);
    }

    @GetMapping("/list-order")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> getGoodsReviewListByOrder(final Pageable pageable, @RequestParam Long ordersId) {
        return goodsReviewService.findAllByOrderId(pageable, ordersId);
    }

    //내 문의 전체 조회
    @GetMapping("/list-my")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> getGoodsReviewMyList(final Pageable pageable, final @AuthenticationPrincipal AccountDetails accountDetails) {
        return goodsReviewService.findAllMyList(pageable, accountDetails.getAccount().getAccountId());
    }

    // ==================== UPDATE ==================== //
    @PatchMapping("/modify")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<GoodsReviewInfoDto> modify(
            final @RequestBody GoodsReviewModifyDto modifyDto, final @AuthenticationPrincipal AccountDetails accountDetails) {
        return goodsReviewService.update(modifyDto,accountDetails);
    }

    // ==================== DELETE ==================== //
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public void deleteGoodsReviewById(@RequestParam Long ordersId, @RequestParam Long goodsId, final @AuthenticationPrincipal AccountDetails accountDetails) {
        goodsReviewService.delete(ordersId, goodsId, accountDetails);
    }

    @DeleteMapping("/delete-image")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public void deleteImages(@RequestBody List<String> imageUrls, String dirName) {
        fileService.deleteMultipleFiles(imageUrls, dirName);
    }

}
