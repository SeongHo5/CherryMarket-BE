package com.cherrydev.cherrymarketbe.goods.controller;

import com.cherrydev.cherrymarketbe.goods.dto.GoodsDto;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsListDto;
import com.cherrydev.cherrymarketbe.goods.service.GoodsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods")
public class GoodsController {

    private final GoodsServiceImpl goodsService;

    /* Insert */
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(final @Valid @RequestBody GoodsDto GoodsDto) {
        goodsService.save(GoodsDto);
    }
    /* Select */
    @GetMapping("/list")
    public List<GoodsListDto> getList(){
        return goodsService.findAll();
    }

    /* Delete */
    @DeleteMapping("/delgoods")
    @ResponseStatus(HttpStatus.CREATED)
    public void delete(Long goodsId) {
        goodsService.deleteById(goodsId);
    }

}
