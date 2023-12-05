package com.cherrydev.cherrymarketbe.cart.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartRequestUpdateDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;
import com.cherrydev.cherrymarketbe.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.cart.service.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartServiceImpl cartService;

    private AccountDetails accountDetails;

    @PostMapping("/refresh")
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity< List<CartResponseDto>> getCartItems (
            final @RequestParam Long accountId
            //final @RequestBody CartRequestDto requestDto
            //final @AuthenticationPrincipal AccountDetails accountDetails
    ){
        return cartService.getCartItems(accountId);
        //return cartService.getCartItems(accountDetails);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addItemToCart (
            final @RequestBody CartRequestDto cartRequestDto)  {
        cartService.addCartItem(cartRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> updateQuantity (
            final @RequestBody CartRequestUpdateDto requestUpdateDto)  {
        cartService.updateQuantity(requestUpdateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCartItem (
            final @RequestParam Long cartId){
        return cartService.deleteCartItem(cartId);
    }

}
