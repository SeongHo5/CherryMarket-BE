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

    @PostMapping("/refresh-available")
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, List<CartResponseDto>>> getAvailableCartItems (
            final @RequestParam Long accountId
            //final @RequestBody CartRequestDto requestDto
            //final @AuthenticationPrincipal AccountDetails accountDetails
    ){  Map<String, List<CartResponseDto>> availableCarts = cartService.getAvailableCarts(accountId);
        return ResponseEntity.ok(availableCarts);

        //return cartService.getCartItems(accountDetails);
    }

    @PostMapping("/refresh-unavailable")
    public ResponseEntity<List<CartResponseDto>> getUnavailableCarts (
            final @RequestParam Long accountId
    ){
        List<CartResponseDto> unavailableCarts = cartService.getUnAvailableCarts(accountId);
        return ResponseEntity.ok(unavailableCarts);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addItemToCart (
            final @RequestBody CartRequestDto cartRequestDto)  {
        cartService.addCartItem(cartRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> updateQuantity (
            final @RequestBody CartRequestChangeDto requestChangeDto)  {
        cartService.updateQuantity(requestChangeDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCartItem (
            final @RequestBody CartRequestChangeDto requestChangeDto){
        cartService.deleteCartItem(requestChangeDto);
        return ResponseEntity.noContent().build();
    }

}
