package com.cherrydev.cherrymarketbe.cart.service;

import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestChangeDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.repository.CartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    @Transactional
    @Override
    public ResponseEntity<Map<String, List<CartResponseDto>>> getAvailableCartItems(Long accountId){

        List<Cart> cartItems = cartMapper.findCartsByAccountId(accountId);
        Map<String,List<CartResponseDto>> groupByStorageTypeItems = cartItems.stream()
                .filter(this::isGoodsAvailable)
                .map(this::convertToResponseDto)
                .collect(Collectors.groupingBy(CartResponseDto::storageType));

        return ResponseEntity.ok(groupByStorageTypeItems);
    }
    @Transactional
    @Override
    public ResponseEntity<List<CartResponseDto>> getUnAvailableCartItems(Long accountId){

        List<Cart> cartItems = cartMapper.findCartsByAccountId(accountId);
        List<CartResponseDto> unavailableCartItems = cartItems.stream()
                .filter(cart -> !isGoodsAvailable(cart))
                .map(this::convertToResponseDto)
                .toList();

        return ResponseEntity.ok(unavailableCartItems);
    }

    @Transactional
    @Override
    public void addCartItem(CartRequestDto requestDto) {
        Cart cart = requestDto.toEntity();
        cartMapper.save(cart);
    }

    @Transactional
    @Override
    public void updateQuantity(CartRequestChangeDto requestChangeDto) {
        Cart cart = requestChangeDto.toEntity();
        cartMapper.updateQuantity(cart);

    }

    @Transactional
    @Override
    public void deleteCartItem(CartRequestChangeDto requestChangeDto) {
        Cart cart = requestChangeDto.toEntity();
        cartMapper.deleteByCartId(cart);
    }

    public boolean isGoodsAvailable(Cart cart) {
        return cart.getGoods().getSalesStatus().equals("ON_SALE");
    }

    private CartResponseDto convertToResponseDto(Cart cart) {
        return CartResponseDto.createCartListItemResponse(cart);
    }

}
