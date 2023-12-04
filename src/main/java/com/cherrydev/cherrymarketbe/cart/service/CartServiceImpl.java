package com.cherrydev.cherrymarketbe.cart.service;


import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.entity.TestGoods;
import com.cherrydev.cherrymarketbe.cart.repository.CartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartMapper cartMapper;


    @Override
    public ResponseEntity<List<CartResponseDto>> getCartItems(CartRequestDto requestDto) {
        List<Cart> cartItems = cartMapper.findCartsByAccountId(requestDto.accountId());
        List<CartResponseDto> responseDtos = cartItems.stream()
                .filter(this::isGoodsAvailable)
                .map(this::convertToResponseDto)
                .toList();

        return ResponseEntity.ok(responseDtos);
    }

    public Cart addCartItem(CartRequestDto requestDto) {
        TestGoods goods = cartMapper.findByGoodsId(requestDto.goodsId())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        return requestDto.toEntity(goods);
    }

    @Override
    public boolean isGoodsAvailable(Cart cart) {

       Long goodsId = cart.getGoods().getGoodsId();

       return true;

//        return cartMapper.findByGoodsId(goodsId)
//                .map(TestGoods::isAvailable) // Goods 클래스에 isAvailable() 메서드를 체크하기
//                .orElse(false);
        //TODO : Goods에서 isAvailable 받아오기.
    }

    private CartResponseDto convertToResponseDto(Cart cart) {
        return CartResponseDto.fromCart(cart);
    }
}
