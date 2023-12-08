package com.cherrydev.cherrymarketbe.cart.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.cart.dto.*;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.repository.CartMapper;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.NOT_FOUND_ACCOUNT;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    @Transactional
    @Override
    public ResponseEntity<CartsByStorageType> getAvailableCarts(AccountDetails accountDetails) {
        validateAccount(accountDetails);
        List<Cart> cartItems = cartMapper.findCartsByAccountId(accountDetails.getAccount().getAccountId());
        return ResponseEntity
                .ok()
                .body(
                        new CartsByStorageType(cartItems)
                );
    }

    @Transactional
    @Override
    public ResponseEntity<UnavailableCarts> getUnavailableCarts(AccountDetails accountDetails) {
        validateAccount(accountDetails);
        List<Cart> cartItems = cartMapper.findCartsByAccountId(accountDetails.getAccount().getAccountId());
        return ResponseEntity
                .ok()
                .body(
                        UnavailableCartsFactory.create(cartItems)
                );
    }

    @Transactional
    @Override
    public void addCartItem(AddCart requestDto, AccountDetails accountDetails) {
        validateAccount(accountDetails);
        Cart cart = requestDto.addCart(accountDetails.getAccount());
        cartMapper.save(cart);
    }

    @Transactional
    @Override
    public void updateQuantity(ChangeCart requestChangeDto) {
        Cart cart = requestChangeDto.toEntity();
        cartMapper.update(cart);
    }

    @Transactional
    @Override
    public void deleteCartItem(Long cartId) {
        cartMapper.delete(cartId);
    }

    private void validateAccount(AccountDetails accountDetails) {
        if (accountDetails == null
                || accountDetails.getAccount() == null
                || accountDetails.getAccount().getAccountId() == null
        ) {
            throw new NotFoundException(NOT_FOUND_ACCOUNT);
        }
    }

}
