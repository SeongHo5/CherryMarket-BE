package com.cherrydev.cherrymarketbe.cart.service;

import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestUpdateDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.repository.CartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    @Transactional
    @Override
    //public ResponseEntity<List<CartResponseDto>> getCartItems(AccountDetails accountDetails) {
    public ResponseEntity<List<CartResponseDto>> getCartItems(Long accountId) {

        //List<Cart> cartItems = cartMapper.findCartsByAccountId(accountDetails.getAccount().getAccountId());
        List<Cart> cartItems = cartMapper.findCartsByAccountId(accountId);
        List<CartResponseDto> responseDtos = cartItems.stream()
                .filter(this::isGoodsAvailable)
                .map(this::convertToResponseDto)
                .toList();

        return ResponseEntity.ok(responseDtos);
    }

    @Transactional
    @Override
    public void addCartItem(CartRequestDto requestDto) {
        Cart cart = requestDto.toEntity();
        cartMapper.save(cart);
    }

    @Transactional
    @Override
    public void updateQuantity(CartRequestUpdateDto requestUpdateDto) {
        Cart cart = requestUpdateDto.toEntity();
        cartMapper.updateQuantity(cart);

    }

    @Transactional
    @Override
    public ResponseEntity<Void> deleteCartItem(Long cartId) {
        cartMapper.deleteByCartId(cartId);
        return ResponseEntity.noContent().build();
    }

    public boolean isGoodsAvailable(Cart cart) {
        return cart.getGoods().getGoodsStatus().equals("ON_SALE");
    }

    private CartResponseDto convertToResponseDto(Cart cart) {
        return CartResponseDto.createResponseForListing(cart);
    }

}
