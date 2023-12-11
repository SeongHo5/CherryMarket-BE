package com.cherrydev.cherrymarketbe.goods.controller;

import com.cherrydev.cherrymarketbe.common.service.FileService;
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
    private final FileService fileService;

    /* Insert */
    @PostMapping("/save")
    public void save(final @Valid @RequestBody GoodsDto goodsDto) {
        goodsService.save(goodsDto);
    }

    /* Select */
    @GetMapping("/listAll")
    public ResponseEntity<List<GoodsDto>> getListAll(@RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(goodsService.findAll(sortBy));
    }

    @GetMapping("/basicInfo")
    public ResponseEntity<DiscountCalcDto> getBasicInfo(@RequestParam Long goodsId) {
        return ResponseEntity.ok(goodsService.findBasicInfo(goodsId));
    }

    @GetMapping("/category")
    public ResponseEntity<List<DiscountCalcDto>> getCategoryGoods(@RequestParam Long categoryId, @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(goodsService.findByCategoryId(categoryId, sortBy));
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
    public ResponseEntity<List<DiscountCalcDto>> getInfoByName(@RequestParam String goodsName, @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(goodsService.findByName(goodsName, sortBy));
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
    public ResponseEntity<List<GoodsDto>> delete(@RequestParam Long goodsId, @RequestParam(required = false) String sortBy) {
        goodsService.deleteById(goodsId);
        return ResponseEntity.ok(goodsService.findAll(sortBy));
    }

    /* 파일 이름 생성 메소드 */
    private String generateFileName(String goodsCode, int type) {
        return goodsCode + "-" + String.format("%03d", type);
    }

}
