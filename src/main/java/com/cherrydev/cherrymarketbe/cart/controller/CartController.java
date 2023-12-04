package com.cherrydev.cherrymarketbe.cart.controller;

import com.cherrydev.cherrymarketbe.cart.dto.CartRequestDto;
import com.cherrydev.cherrymarketbe.cart.dto.CartResponseDto;
import com.cherrydev.cherrymarketbe.cart.service.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartServiceImpl cartService;

    @GetMapping("/v1/refresh")
    public ResponseEntity<List<CartResponseDto>> getCartItems(@RequestBody CartRequestDto requestDto){
        return cartService.getCartItems(requestDto);
    }

}
