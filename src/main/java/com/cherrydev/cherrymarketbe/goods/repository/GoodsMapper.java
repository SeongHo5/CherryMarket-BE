package com.cherrydev.cherrymarketbe.goods.repository;

import com.cherrydev.cherrymarketbe.goods.dto.GoodsListDto;
import com.cherrydev.cherrymarketbe.goods.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {
    void saveGoods(Goods goods);

    List<GoodsListDto> findAllGoods();

    void deleteGoodsById(Long goodsId);
}
