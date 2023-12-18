package com.cherrydev.cherrymarketbe.goods.repository;

import com.cherrydev.cherrymarketbe.goods.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {

    /* Insert */
    void save(GoodsDto goodsDto);

    /* Select */
    List<GoodsDto> findAll(String sortBy);

    GoodsDto findById(Long goodsId);

    List<GoodsDto> findByMakerForDiscount(Long makerId);

    List<GoodsDto> findByCategoryForDiscount(Long categoryId);

    GoodsDto findByGoodsIdForDiscount(Long goodsId);

    GoodsBasicInfoDto findBasicInfo(Long goodsId);

    List<GoodsBasicInfoDto> findByCategoryId(Long categoryId, String sortBy);

    ToCartResponseDto findToCart(Long goodsId);

    GoodsDetailDto findDetailById(Long goodsId);

    GoodsDetailDto findDetailByCode(String goodsCode);

    List<GoodsBasicInfoDto> findByName(String goodsName, String sortBy);

    GoodsInventoryResponseDto findInventoryByGoodsId(Long goodsId);

    List<GoodsBasicInfoDto> findNewGoods();

    List<GoodsBasicInfoDto> findDiscountGoods();


    /* Update */
    void updateStatusWhenNewGoods(String goodsCode, String salesStatus);

    void updateDiscountByMaker(Long discountId, Long makerId);

    void updateDiscountByCategory(Long discountId, Long categoryId);

    void updateDiscountByGoodsId(Long discountId, Long goodsId);

    void updateGoodsInventory(Long goodsId, int quantity);


    /* Delete */
    void deleteById(Long goodsId);

}
