package com.cherrydev.cherrymarketbe.cart.repository;

import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.entity.TestGoods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

   void save(Cart cart);

   List<Cart> findCartsByAccountId(Long accountId);

   void updateQuantity(Cart cart);

   void deleteByCartId(Long cartId);


}
