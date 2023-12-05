package com.cherrydev.cherrymarketbe.cart.service;


import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestChangeDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;

import java.util.List;
import java.util.Map;

public interface CartService {

    Map<String, List<CartResponseDto>> getAvailableCarts(Long accountId);
    List<CartResponseDto> getUnAvailableCarts(Long accountId);
    void addCartItem(CartRequestDto requestDto);
    void deleteCartItem(CartRequestChangeDto requestChangeDto);
    void updateQuantity(CartRequestChangeDto requestChangeDto);

}
