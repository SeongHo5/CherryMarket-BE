package com.cherrydev.cherrymarketbe.goods.controller;

import com.cherrydev.cherrymarketbe.goods.dto.DiscountCalcDto;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsDetailResponseDto;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsDto;
import com.cherrydev.cherrymarketbe.goods.dto.ToCartResponseDto;
import com.cherrydev.cherrymarketbe.goods.service.GoodsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods")
public class GoodsController {

    private final GoodsService goodsService;

    /* Insert */
    @PostMapping("/save")
    public void save(final @Valid @RequestBody GoodsDto goodsDto) {
        goodsService.save(goodsDto);
    }

    /* Select */
    @GetMapping("/listAll")
    public ResponseEntity<List<GoodsDto>> getListAll() {
        return ResponseEntity.ok(goodsService.findAll());
    }

    @GetMapping("/basicInfo")
    public ResponseEntity<DiscountCalcDto> getBasicInfo(@RequestParam Long goodsId) {
        return ResponseEntity.ok(goodsService.findBasicInfo(goodsId));
    }

    @GetMapping("/category")
    public ResponseEntity<List<DiscountCalcDto>> getCategoryGoods(@RequestParam Long categoryId) {
        return ResponseEntity.ok(goodsService.findByCategoryId(categoryId));
    }

    @GetMapping("/toCart")
    public ResponseEntity<ToCartResponseDto> getToCart(@RequestParam Long goodsId) {
        return ResponseEntity.ok(goodsService.findToCart(goodsId));
    }

    @GetMapping("/detail")
    public ResponseEntity<GoodsDetailResponseDto> getDetailById(@RequestParam Long goodsId) {
        return ResponseEntity.ok(goodsService.findDetailById(goodsId));
    }

    @GetMapping("/{goodsCode}")
    public ResponseEntity<GoodsDetailResponseDto> getDetailByCode(@PathVariable String goodsCode) {
        return ResponseEntity.ok(goodsService.findDetailByCode(goodsCode));
    }

    @GetMapping("/name")
    public ResponseEntity<List<DiscountCalcDto>> getInfoByName(@RequestParam String goodsName) {
        return ResponseEntity.ok(goodsService.findByName(goodsName));
    }

    /* Update */
    @PatchMapping("/update/discount/maker")
    public ResponseEntity<List<GoodsDto>> updateDiscountByMaker(@RequestParam Long discountId, @RequestParam Long makerId) {

        return ResponseEntity.ok(goodsService.updateDiscountByMaker(discountId, makerId));
    }

    @PatchMapping("/update/discount/category")
    public ResponseEntity<List<GoodsDto>> updateDiscountByCategory(@RequestParam Long discountId, @RequestParam Long categoryId) {
        return ResponseEntity.ok(goodsService.updateDiscountByCategory(discountId, categoryId));
    }

    @PatchMapping("/update/discount/goodsId")
    public ResponseEntity<GoodsDto> updateDiscountByGoodsId(@RequestParam Long discountId, @RequestParam Long goodsId) {
        return ResponseEntity.ok(goodsService.updateDiscountByGoodsId(discountId, goodsId));
    }

    /* Delete */
    @DeleteMapping("/delete")
    public ResponseEntity<List<GoodsDto>> delete(Long goodsId) {
        goodsService.deleteById(goodsId);
        return ResponseEntity.ok(goodsService.findAll());
    }

}
