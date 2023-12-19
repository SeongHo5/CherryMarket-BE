package com.cherrydev.cherrymarketbe.goods.controller;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.common.service.FileService;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsBasicInfoResponseDto;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsDetailResponseDto;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsDto;
import com.cherrydev.cherrymarketbe.goods.service.GoodsService;
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
    private final FileService fileService;

    /* Insert */
    @PostMapping("/save")
    public ResponseEntity<String> save(@Valid @ModelAttribute GoodsDto goodsDto,
                                       @RequestParam List<MultipartFile> images) {
        goodsService.save(goodsDto, images);
        return ResponseEntity.ok("상품이 정상적으로 등록되었습니다");
    }

    /* Select */
    @GetMapping("/listAll")
    public ResponseEntity<MyPage<GoodsBasicInfoResponseDto>> getListAll(final Pageable pageable, @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(goodsService.findAll(pageable, sortBy));
    }

    @GetMapping("/basicInfo")
    public ResponseEntity<GoodsBasicInfoResponseDto> getBasicInfo(@RequestParam Long goodsId) {
        return ResponseEntity.ok(goodsService.findBasicInfo(goodsId));
    }

    @GetMapping("/category")
    public ResponseEntity<MyPage<GoodsBasicInfoResponseDto>> getCategoryGoods(final Pageable pageable, @RequestParam Long categoryId, @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(goodsService.findByCategoryId(pageable, categoryId, sortBy));
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
    public ResponseEntity<MyPage<GoodsBasicInfoResponseDto>> getInfoByName(final Pageable pageable, @RequestParam String goodsName, @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(goodsService.findByName(pageable, goodsName, sortBy));
    }

    @GetMapping("/findNew")
    public ResponseEntity<List<GoodsBasicInfoResponseDto>> getNewGoods() {
        return ResponseEntity.ok(goodsService.findNewGoods());
    }

    @GetMapping("/findDiscount")
    public ResponseEntity<List<GoodsBasicInfoResponseDto>> getDiscountGoods() {
        return ResponseEntity.ok(goodsService.findDiscountGoods());
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
    public ResponseEntity<MyPage<GoodsBasicInfoResponseDto>> delete(@RequestParam Long goodsId, final Pageable pageable, @RequestParam(required = false) String sortBy) {
        goodsService.deleteById(goodsId);
        return ResponseEntity.ok(goodsService.findAll(pageable, sortBy));
    }
}
