package com.cherrydev.cherrymarketbe.cart.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;

import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestChangeDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;
import com.cherrydev.cherrymarketbe.cart.service.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartServiceImpl cartService;

    private AccountDetails accountDetails;

    /**
     * 장바구니 리스트 - 주문 가능한 상품
     */
    @GetMapping ("/refresh-available")
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, List<CartResponseDto>>> getAvailableCarts(
            final @AuthenticationPrincipal AccountDetails accountDetails){

        Map<String, List<CartResponseDto>> availableCarts = cartService.getAvailableCarts(accountDetails);
        return ResponseEntity.ok(availableCarts);
    }


    /**
     * 장바구니 리스트 - 주문 불가 상품
     */
    @GetMapping("/refresh-unavailable")
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CartResponseDto>> getUnavailableCarts (
            final @AuthenticationPrincipal AccountDetails accountDetails){

        List<CartResponseDto> unavailableCarts = cartService.getUnavailableCarts(accountDetails);
        return ResponseEntity.ok(unavailableCarts);
    }

    /**
     * 장바구니 상품 추가
     */
    @PostMapping("/add")
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> addItemToCart (
            final @RequestBody CartRequestDto cartRequestDto,
            final @AuthenticationPrincipal AccountDetails accountDetails)  {

        cartService.addCartItem(cartRequestDto, accountDetails);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 장바구니 상품 수량 변경
     */
    @PatchMapping("/update")
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateQuantity (
            final @RequestBody CartRequestChangeDto requestChangeDto)  {

        cartService.updateQuantity(requestChangeDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 장바구니 상품 삭제
     */
    @DeleteMapping("/delete")
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCartItem (
            final @RequestParam Long cartId){

        cartService.deleteCartItem(cartId);
        return ResponseEntity.noContent().build();
    }

}
