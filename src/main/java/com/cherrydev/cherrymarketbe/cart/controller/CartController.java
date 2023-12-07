package com.cherrydev.cherrymarketbe.cart.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;

import com.cherrydev.cherrymarketbe.cart.dto.*;
import com.cherrydev.cherrymarketbe.cart.service.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartServiceImpl cartService;

    /**
     * 장바구니 리스트 - 주문 가능한 상품
     */
    @GetMapping ("/refresh-available")
    @ResponseStatus(HttpStatus.OK)
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<CartsByStorageType> getAvailableCarts (
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return cartService.getAvailableCarts(accountDetails);
    }

    /**
     * 장바구니 리스트 - 주문 불가 상품
     */
    @GetMapping("/refresh-unavailable")
    @ResponseStatus(HttpStatus.OK)
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UnavailableCarts> getUnavailableCarts (
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return cartService.getUnavailableCarts(accountDetails);
    }

    /**
     * 장바구니 상품 추가
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public void addItemToCart (
            final @RequestBody AddCart addCart,
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        cartService.addCartItem(addCart, accountDetails);
    }

    /**
     * 장바구니 상품 수량 변경
     */
    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public void updateQuantity (
            final @RequestBody ChangeCart requestChangeDto
    ) {
        cartService.updateQuantity(requestChangeDto);
    }

    /**
     * 장바구니 상품 삭제
     */
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public void deleteCartItem (
            final @RequestParam Long cartId
    ) {
       cartService.deleteCartItem(cartId);
    }

}
