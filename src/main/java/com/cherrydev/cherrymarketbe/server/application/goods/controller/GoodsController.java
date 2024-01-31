package com.cherrydev.cherrymarketbe.server.application.goods.controller;

import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsService;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.MyPage;
import com.cherrydev.cherrymarketbe.server.domain.discount.dto.request.UpdateDiscountCondition;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsDetailInfo;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.RequestAddGoods;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.SearchCondition;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods")
public class GoodsController {

    private final GoodsService goodsService;

    @PostMapping("/save")
    public ResponseEntity<Void> save(
            @RequestBody @Valid RequestAddGoods requestAddGoods,
            @RequestParam List<MultipartFile> images
    ) {
        goodsService.save(requestAddGoods, images);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list-all")
    public ResponseEntity<MyPage<GoodsInfo>> getListAll(Pageable pageable) {
        return ResponseEntity.ok(goodsService.findAll(pageable));
    }

    @GetMapping("/list-all/conditions")
    public ResponseEntity<MyPage<GoodsInfo> > getAllListByConditions(
            Pageable pageable,
            @RequestParam(name = "goods_name", required = false) final String goodsName,
            @RequestParam(name = "category_id",required = false) final Long categoryId,
            @RequestParam(name = "maker_id",required = false) final Long makerId,
            @RequestParam(name = "goods_code",required = false) final String goodsCode,
            @RequestParam(name = "sales_status",required = false) final String salesStatus
    ) {
        SearchCondition searchCondition = new SearchCondition(goodsName, categoryId, makerId, goodsCode, salesStatus);
        return ResponseEntity.ok(goodsService.findAllByConditions(pageable, searchCondition));
    }

    @GetMapping("/list-all/detail/conditions")
    public ResponseEntity<MyPage<GoodsDetailInfo> > getDetailedListByConditions(
            Pageable pageable,
            @RequestParam(name = "goods_name", required = false) final String goodsName,
            @RequestParam(name = "category_id",required = false) final Long categoryId,
            @RequestParam(name = "maker_id",required = false) final Long makerId,
            @RequestParam(name = "goods_code",required = false) final String goodsCode,
            @RequestParam(name = "sales_status",required = false) final String salesStatus
    ) {
        SearchCondition searchCondition = new SearchCondition(goodsName, categoryId, makerId, goodsCode, salesStatus);
        return ResponseEntity.ok(goodsService.findAllDetailedByConditions(pageable, searchCondition));
    }

    @GetMapping("/on-sale")
    public ResponseEntity<MyPage<GoodsInfo>> getDiscountGoods(Pageable pageable) {
        return ResponseEntity.ok(goodsService.getDiscountedGoods(pageable));
    }

    @PatchMapping("/update/condition")
    public ResponseEntity<Integer> updateDiscountByCondition(
            @RequestParam(name = "discount_id") Long discountId,
            @RequestParam(name = "maker_id", required = false) Long makerId,
            @RequestParam(name = "category_id", required = false) Long categoryId,
            @RequestParam(name = "goods_id", required = false) Long goodsId
    ) {
        UpdateDiscountCondition condition = new UpdateDiscountCondition(makerId, categoryId, goodsId);
        return ResponseEntity.ok(goodsService.updateDiscountByConditions(discountId, condition));
    }

    /* Delete */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam final Long goodsId) {
        goodsService.deleteById(goodsId);
        return ResponseEntity.ok().build();
    }
}
