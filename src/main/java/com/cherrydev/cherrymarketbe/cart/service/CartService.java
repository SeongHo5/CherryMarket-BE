package com.cherrydev.cherrymarketbe.cart.service;


import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestChangeDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CartService {

    ResponseEntity<Map<String, List<CartResponseDto>>> getAvailableCartItems(Long accountId);
    ResponseEntity<List<CartResponseDto>> getUnAvailableCartItems(Long accountId);
    void addCartItem(CartRequestDto requestDto);
    void deleteCartItem(CartRequestChangeDto requestChangeDto);
    void updateQuantity(CartRequestChangeDto requestChangeDto);

}
