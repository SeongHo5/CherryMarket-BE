package com.cherrydev.cherrymarketbe.cart.service;


import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestChangeDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;

import java.util.List;
import java.util.Map;

public interface CartService {

    Map<String, List<CartResponseDto>> getAvailableCarts(AccountDetails accountDetails);
    List<CartResponseDto> getUnavailableCarts(AccountDetails accountDetails);
    void addCartItem(CartRequestDto requestDto, AccountDetails accountDetails);
    void deleteCartItem(Long cartId);
    void updateQuantity(CartRequestChangeDto requestChangeDto);

}
