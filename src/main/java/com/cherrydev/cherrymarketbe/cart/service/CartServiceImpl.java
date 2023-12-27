package com.cherrydev.cherrymarketbe.cart.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.cart.dto.*;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.repository.CartMapper;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.goods.dto.ToCartResponseDto;
import com.cherrydev.cherrymarketbe.goods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.CONFLICT_CART_ITEM;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final GoodsService goodsService;

    @Transactional
    @Override
    public ResponseEntity<CartsByStorageType> getAvailableCarts(AccountDetails accountDetails) {
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
        ToCartResponseDto responseDto = goodsService.findToCart(requestDto.goodsId());

        Cart cart = requestDto.getCart(accountDetails, responseDto);

        boolean isExist = cartMapper.existsByAccountIdAndGoodsId(accountDetails.getAccount().getAccountId(), requestDto.goodsId());

        if (isExist) {
            throw new DuplicatedException(CONFLICT_CART_ITEM);
        }

        boolean checkInventory = goodsService.findInventory(requestDto.goodsId(), requestDto.quantity());
        if(checkInventory) {
            goodsService.updateGoodsInventory(requestDto.goodsId(), requestDto.quantity());
        }

        cartMapper.save(cart);
    }

    @Transactional
    @Override
    public void updateQuantity(ChangeCart requestChangeDto) {
        Cart cart = requestChangeDto.toEntity();
        Long findGoodsId = cartMapper.findGoodsIdByCartId(requestChangeDto.cartId());

        boolean checkInventory = goodsService.findInventory(findGoodsId, requestChangeDto.quantity());
        if(checkInventory) {
            cartMapper.update(cart);
        }
    }

    @Transactional
    @Override
    public void deleteCartItem(Long cartId) {
        cartMapper.delete(cartId);
    }

}
