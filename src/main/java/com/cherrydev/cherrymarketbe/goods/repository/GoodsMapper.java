package com.cherrydev.cherrymarketbe.goods.repository;

import com.cherrydev.cherrymarketbe.goods.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {

    void save(GoodsRegistrationDto goodsRegistrationDto);

    List<GoodsRegistrationDto> findAll();

    GoodsBasicInfoDto findBasicInfo(Long goodsId);

    List<GoodsBasicInfoDto> findByCategoryId(Long categoryId);

    ToCartDto findToCart(Long goodsId);

    GoodsDetailDto findDetailById(Long goodsId);

    GoodsDetailDto findDetailByCode(String goodsCode);

    void updateStatusWhenNewGoods(String goodsCode, String salesStatus );

    void deleteById(Long goodId);

}
