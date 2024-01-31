package com.cherrydev.cherrymarketbe.server.application.order.controller;

import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.PaymentDetailsInfo;
import com.cherrydev.cherrymarketbe.server.application.order.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order/payment-detail")
public class PaymentDetailController {

    private final PaymentService paymentService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{orderCode}")
    public ResponseEntity<PaymentDetailsInfo> getPaymentDetailsByOrderCode(@PathVariable String orderCode) {
        return paymentService.findPaymentDetailsByOrderCode(orderCode);
    }

}
