package com.cherrydev.cherrymarketbe.goods.repository;

import com.cherrydev.cherrymarketbe.goods.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {

    void save(GoodsRegistrationDto goodsRegistrationDto);

    List<GoodsRegistrationDto> findAll();

    GoodsBasicInfoDto findBasicInfo(Long goodsId);

    ToCartDto findToCart(Long goodsId);

    GoodsDetailDto findDetail(Long goodsId);

    void deleteById(Long goodId);

}
