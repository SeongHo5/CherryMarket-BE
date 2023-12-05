package com.cherrydev.cherrymarketbe.goods.service;

import com.cherrydev.cherrymarketbe.goods.dto.GoodsDto;
import com.cherrydev.cherrymarketbe.goods.dto.GoodsListDto;
import com.cherrydev.cherrymarketbe.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.goods.repository.GoodsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;


    @Override
    @Transactional
    public void addGoods(final GoodsDto goodsDto) {
        Goods goods = goodsDto.toEntity();
        goodsMapper.saveGoods(goods);
    }

    @Override
    @Transactional
    public List<GoodsListDto> findAllGoods() {
        List<GoodsListDto> goodsList = goodsMapper.findAllGoods();
        return goodsList;
    }

    @Override
    @Transactional
    public void deleteGoodsById(Long goodsId) {
        goodsMapper.deleteGoodsById(goodsId);

    }
}
