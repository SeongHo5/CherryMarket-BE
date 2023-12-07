package com.cherrydev.cherrymarketbe.order.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.dto.AllUserOrderList;
import com.cherrydev.cherrymarketbe.order.dto.ChangeOrderStatus;
import com.cherrydev.cherrymarketbe.order.dto.UserOrderList;
import com.cherrydev.cherrymarketbe.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    /**
     * 주문 목록 - 전체 회원
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list-allUsers")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AllUserOrderList> getOrders() {
        return orderServiceImpl.findAllOrders();
    }

    /**
     * 주문 목록 - 로그인 한 회원
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list")
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserOrderList> getOrders (
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return orderServiceImpl.findOrdersByAccountId(accountDetails);
    }

    /**
     * 주문 생성
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    //@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public void createOrder (
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        orderServiceImpl.createOrder(accountDetails);
    }

    /**
     * 주문 상태 변경
     */
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping ("/change-status")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateOrderStatus (
            final @RequestBody ChangeOrderStatus requestChangeDto
    ) {
        orderServiceImpl.updateOrderStatus(requestChangeDto);
    }

}
