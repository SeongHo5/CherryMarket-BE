package com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods;

import com.cherrydev.cherrymarketbe.server.domain.discount.dto.request.UpdateDiscountCondition;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.*;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface GoodsMapper {

    /* Insert */
    void save(RequestAddGoods requestAddGoods);

    /* Select */
    Optional<Goods> findById(Long goodsId);

    List<Goods> findAll();

    List<Goods> searchGoods(Map<String, Object> params);

    ToCartResponse findToCart(Long goodsId);

    GoodsInventoryResponseDto findInventoryByGoodsId(Long goodsId);

    List<Goods> findDiscountedGoods();

    Integer updateDiscount(Long discountId, UpdateDiscountCondition condition);

    /* Update */
    void updateStatusWhenNewGoods(String goodsCode, String salesStatus);

    void updateGoodsInventory(Long goodsId, int quantity);

    void delete(Goods goods);

}
