package com.cherrydev.cherrymarketbe.server.application.cart.service;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.cart.dto.request.AddCart;
import com.cherrydev.cherrymarketbe.server.domain.cart.dto.request.ChangeCart;
import com.cherrydev.cherrymarketbe.server.domain.cart.dto.response.CartsByStorageType;
import com.cherrydev.cherrymarketbe.server.domain.cart.dto.response.UnavailableCarts;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity<CartsByStorageType> getAvailableCarts(AccountDetails accountDetails);
    ResponseEntity<UnavailableCarts> getUnavailableCarts(AccountDetails accountDetails);
    void addCartItem(AddCart requestDto, AccountDetails accountDetails);
    void deleteCartItem(Long cartId);
    void updateQuantity(ChangeCart requestChangeDto);

}
