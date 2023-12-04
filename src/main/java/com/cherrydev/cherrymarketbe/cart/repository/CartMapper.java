package com.cherrydev.cherrymarketbe.cart.repository;


import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.entity.TestGoods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CartMapper {

   void save(CartRequestDto cartRequestDto);

   List<Cart> findCartsByAccountId(Long accountId);

   void updateGoodsQuantity(CartRequestDto cartRequestDto);

   void deleteItemByCartId(Long cartId);

   Optional<TestGoods> findByGoodsId(Long goodsId);

}
