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
    public void save(final GoodsDto goodsDto) {
        Goods goods = goodsDto.toEntity();
        goodsMapper.save(goods);
    }

    @Override
    @Transactional
    public List<GoodsListDto> findAll() {
        return goodsMapper.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long goodsId) {
        goodsMapper.deleteById(goodsId);

    }
}
