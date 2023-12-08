package com.cherrydev.cherrymarketbe.cart.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.cart.dto.*;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity<CartsByStorageType> getAvailableCarts(AccountDetails accountDetails);
    ResponseEntity<UnavailableCarts> getUnavailableCarts(AccountDetails accountDetails);
    void addCartItem(AddCart requestDto, AccountDetails accountDetails);
    void deleteCartItem(Long cartId);
    void updateQuantity(ChangeCart requestChangeDto);

}
