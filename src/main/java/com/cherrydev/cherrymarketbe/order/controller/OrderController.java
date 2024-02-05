package com.cherrydev.cherrymarketbe.order.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderDetailsInfo;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderListGroupByStatusResponse;
import com.cherrydev.cherrymarketbe.order.dto.requests.OrderStatusRequest;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderSummaryList;
import com.cherrydev.cherrymarketbe.order.service.OrderServiceImpl;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentConfirmForm;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderListGroupByStatusResponse> getOrders() {
        return orderServiceImpl.findAllUserOrders();
    }

    /**
     * 주문 목록 - 전체 회원
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list-status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderListGroupByStatusResponse> getOrdersByStatus(
            @RequestParam String orderStatus
    ) {
        return orderServiceImpl.findAllUserOrdersByStatus(orderStatus);
    }

    /**
     * 주문정보 요약 목록 - 로그인 한 회원
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderSummaryList> getOrderSummaryList (
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return orderServiceImpl.findOrdersByAccountId(accountDetails);
    }

    /**
     * 주문건 상세 정보
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list/{orderCode}/order-details")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderDetailsInfo> getOrderDetails (
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @PathVariable String orderCode
    ) {
        return orderServiceImpl.findOrderDetails(accountDetails, orderCode);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Payment> confirmPayment(
            @RequestParam String orderId,
            @RequestParam String paymentKey,
            @RequestParam Number amount) {

        PaymentConfirmForm form = new PaymentConfirmForm(paymentKey, orderId, amount);

        return ResponseEntity.ok(orderServiceImpl.confirmPaymentInfo(form));
    }

    /**
     * 주문 생성
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public void createOrder (
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestBody OrderReceiptResponse responseDto
    ) {
        orderServiceImpl.createOrder(accountDetails, responseDto);
    }

    /**
     * 주문 상태 변경
     * <P>
     * 주문 상태가 'COMPLETED' 로 변경될 경우 구매 금액의 10%가 리워드로 지급됩니다.
     */
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping ("/change-status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateOrderStatus (
            final @RequestBody OrderStatusRequest requestChangeDto
    ) {
        orderServiceImpl.updateOrderStatus(requestChangeDto);
    }

    @DeleteMapping("/list/{orderCode}/cancel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> cancelOrder(
            final @PathVariable String orderCode,
            final @RequestBody PaymentCancelForm form) {

        orderServiceImpl.cancelOrder(orderCode, form);
        return ResponseEntity.ok().build();

    }
}
