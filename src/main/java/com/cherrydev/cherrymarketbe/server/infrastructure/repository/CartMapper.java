package com.cherrydev.cherrymarketbe.server.infrastructure.repository;

import com.cherrydev.cherrymarketbe.server.domain.cart.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    void save(Cart cart);

    List<Cart> findCartsByAccountId(Long accountId);

    void update(Cart cart);

    void delete(Long cartId);

    Cart findByCartId(Long cartId);

    boolean existsByAccountIdAndGoodsId(@Param("accountId") Long accountId, @Param("goodsId") Long goodsId);

    Long findGoodsIdByCartId(Long cartId);
}
