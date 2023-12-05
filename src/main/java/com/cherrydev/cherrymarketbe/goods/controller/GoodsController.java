package com.cherrydev.cherrymarketbe.goods.controller;

import com.cherrydev.cherrymarketbe.goods.dto.GoodsDto;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsListDto;
import com.cherrydev.cherrymarketbe.goods.service.GoodsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods")
public class GoodsController {

    private final GoodsServiceImpl goodsService;

    /* Insert */
    @PostMapping("/addgoods")
    @ResponseStatus(HttpStatus.CREATED)
    public void addGoods(final @Valid @RequestBody GoodsDto GoodsDto) {
        goodsService.addGoods(GoodsDto);
    }
    /* Select */
    @GetMapping("/goodslist")
    public List<GoodsListDto> getGoodsList(){
        return goodsService.findAllGoods();
    }

    /* Delete */
    @DeleteMapping("/delgoods")
    @ResponseStatus(HttpStatus.CREATED)
    public void delGoods(Long goodsId) {
        goodsService.deleteGoodsById(goodsId);
    }

}
