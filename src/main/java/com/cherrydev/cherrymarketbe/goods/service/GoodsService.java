package com.cherrydev.cherrymarketbe.goods.service;

import com.cherrydev.cherrymarketbe.goods.dto.GoodsDto;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsListDto;

import java.util.List;

public interface GoodsService {

    void save(final GoodsDto goodsDto);

    List<GoodsListDto> findAll();

    void deleteById(Long goodsId);
}
