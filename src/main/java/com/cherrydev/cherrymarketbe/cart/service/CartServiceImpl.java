package com.cherrydev.cherrymarketbe.cart.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestChangeDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.repository.CartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Map<String, List<CartResponseDto>> getAvailableCarts(AccountDetails accountDetails) {
        List<Cart> cartItems = cartMapper.findCartsByAccountId(getAccountId(accountDetails));

        return cartItems.stream()
                .filter(Cart::isGoodsAvailable)
                .map(CartResponseDto::getCartsList)
                .collect(Collectors.groupingBy(CartResponseDto::storageType));
    }
    @Transactional
    @Override
    public List<CartResponseDto> getUnavailableCarts(AccountDetails accountDetails) {
        List<Cart> cartItems = cartMapper.findCartsByAccountId(getAccountId(accountDetails));

        return cartItems.stream()
                .filter(cart -> !cart.isGoodsAvailable())
                .map(CartResponseDto::getCartsList)
                .toList();
    }

    @Transactional
    @Override
    public void addCartItem(CartRequestDto requestDto, AccountDetails accountDetails) {
        Cart cart = requestDto.addCart(getAccount(accountDetails));
        cartMapper.save(cart);
    }

    @Transactional
    @Override
    public void updateQuantity(CartRequestChangeDto requestChangeDto) {
        Cart cart = requestChangeDto.toEntity();
        cartMapper.update(cart);

    }
    @Transactional
    @Override
    public void deleteCartItem(Long cartId) {
        cartMapper.delete(cartId);
    }

    public Long getAccountId(AccountDetails accountDetails){
        return accountDetails.getAccount().getAccountId();
    }

    public Account getAccount(AccountDetails accountDetails){
        return accountDetails.getAccount();
    }


}
