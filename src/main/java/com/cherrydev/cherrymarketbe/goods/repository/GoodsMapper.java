package com.cherrydev.cherrymarketbe.goods.repository;

import com.cherrydev.cherrymarketbe.goods.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {

    /* Insert */
    void save(GoodsDto goodsDto);

    /* Select */
    List<GoodsDto> findAll();

    List<GoodsDto> findByMakerForDiscount(Long makerId);

    List<GoodsDto> findByCategoryForDiscount(Long makerId);

    GoodsDto findByGoodsIdForDiscount(Long makerId);

    GoodsBasicInfoDto findBasicInfo(Long goodsId);

    List<GoodsBasicInfoDto> findByCategoryId(Long categoryId);

    ToCartDto findToCart(Long goodsId);

    GoodsDetailDto findDetailById(Long goodsId);

    GoodsDetailDto findDetailByCode(String goodsCode);

    List<GoodsBasicInfoDto> findByName(String goodsName);

    /* Update */
    void updateStatusWhenNewGoods(String goodsCode, String salesStatus);

    void updateDiscountByMaker(Long discountId, Long makerId);

    void updateDiscountByCategory(Long discountId, Long categoryId);

    void updateDiscountByGoodsId(Long discountId, Long goodsId);

    /* Delete */
    void deleteById(Long goodId);

}
