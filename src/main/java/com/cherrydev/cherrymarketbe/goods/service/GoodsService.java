package com.cherrydev.cherrymarketbe.goods.service;

import com.cherrydev.cherrymarketbe.goods.dto.GoodsDto;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsListDto;

import java.util.List;

public interface GoodsService {

    void addGoods(final GoodsDto GoodsDto);

    List<GoodsListDto> findAllGoods();

    void deleteGoodsById(Long goodsId);
}
