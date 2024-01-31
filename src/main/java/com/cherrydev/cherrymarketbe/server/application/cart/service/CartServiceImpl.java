package com.cherrydev.cherrymarketbe.server.application.cart.service;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.CartMapper;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.server.domain.cart.dto.request.AddCart;
import com.cherrydev.cherrymarketbe.server.domain.cart.dto.request.ChangeCart;
import com.cherrydev.cherrymarketbe.server.domain.cart.dto.response.CartsByStorageType;
import com.cherrydev.cherrymarketbe.server.domain.cart.dto.response.UnavailableCarts;
import com.cherrydev.cherrymarketbe.server.domain.cart.dto.response.UnavailableCartsFactory;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.ToCartResponse;
import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.CONFLICT_CART_ITEM;

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
        ToCartResponse responseDto = goodsService.findToCart(requestDto.goodsId());

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
