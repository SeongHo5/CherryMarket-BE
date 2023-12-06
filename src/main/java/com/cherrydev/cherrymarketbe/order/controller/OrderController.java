package com.cherrydev.cherrymarketbe.order.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.dto.OrderCreateDto;
import com.cherrydev.cherrymarketbe.order.dto.OrderRequestChangeDto;
import com.cherrydev.cherrymarketbe.order.dto.OrderResponseDto;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import com.cherrydev.cherrymarketbe.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    /**
     * 주문 목록 - 전체 회원
     */
    @GetMapping("/list-allUsers")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<OrderStatus,List<OrderResponseDto>>> getOrders(){
       Map<OrderStatus,List<OrderResponseDto>> orders = orderServiceImpl.findAllOrders();
       return ResponseEntity.ok(orders);
    }



    /**
     * 주문 목록 - 로그인 한 회원
     */
    @GetMapping("/list")
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getOrders(
            final @AuthenticationPrincipal AccountDetails accountDetails){
        List<OrderResponseDto> orders = orderServiceImpl.findOrdersByAccountId(accountDetails);
        return ResponseEntity.ok(orders);
    }

    /**
     * 주문 생성
     */
    @PostMapping("/add")
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createOrder (
            final @AuthenticationPrincipal AccountDetails accountDetails){
        orderServiceImpl.createOrder(accountDetails);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 주문 상태 변경
     */
    @PatchMapping ("/change-status")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateOrderStatus (
            final @RequestBody OrderRequestChangeDto requestChangeDto) {
        orderServiceImpl.updateOrderStatus(requestChangeDto);
        return ResponseEntity.ok().build();
    }

}
