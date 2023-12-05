package com.cherrydev.cherrymarketbe.cart.service;


import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestUpdateDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CartService {


    ResponseEntity<Map<String, List<CartResponseDto>>> getAvailableCartItems(Long accountId);

   // ResponseEntity<List<CartResponseDto>> getCartItems(Long accountId);
    //ResponseEntity<List<CartResponseDto>> getCartItems(AccountDetails accountDetails);

//    ResponseEntity<List<CartResponseDto>> getFrozenItemsInCart(CartRequestDto requestDto);
//    ResponseEntity<List<CartResponseDto>> getRoomTemperatureItemsInCart(CartRequestDto requestDto);
//    ResponseEntity<List<CartResponseDto>> getRefrigeratedItemsInCart(CartRequestDto requestDto);
//    ResponseEntity<List<CartResponseDto>> getUnavailableItemsInCart(CartRequestDto requestDto);

    void addCartItem(CartRequestDto requestDto);
    ResponseEntity<Void> deleteCartItem(Long cartId);
    void updateQuantity(CartRequestUpdateDto requestUpdateDto);

}
