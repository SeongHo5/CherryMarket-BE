package com.cherrydev.cherrymarketbe.cart.service;

import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {

    ResponseEntity<List<CartResponseDto>> getCartItems(CartRequestDto requestDto);

//    ResponseEntity<List<CartResponseDto>> getFrozenItemsInCart(CartRequestDto requestDto);
//    ResponseEntity<List<CartResponseDto>> getRoomTemperatureItemsInCart(CartRequestDto requestDto);
//    ResponseEntity<List<CartResponseDto>> getRefrigeratedItemsInCart(CartRequestDto requestDto);
//    ResponseEntity<List<CartResponseDto>> getUnavailableItemsInCart(CartRequestDto requestDto);


    boolean isGoodsAvailable(Cart cart);

}
