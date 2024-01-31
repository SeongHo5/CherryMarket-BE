package com.cherrydev.cherrymarketbe.server.application.payments.service;

import com.cherrydev.cherrymarketbe.server.application.config.feign.TossFeignConfig;
import com.cherrydev.cherrymarketbe.server.domain.payment.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.dto.PaymentApproveForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.model.cardpromotion.CardPromotion;
import com.cherrydev.cherrymarketbe.server.domain.payment.model.payment.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "tossPayment", url = "https://api.tosspayments.com/v1/payments", configuration = TossFeignConfig.class)
public interface TossFeignClient {

    @GetMapping("/orders/{orderId}")
    Payment findPaymentByOrderId(
            @PathVariable("orderId") String orderId
    );

    @GetMapping("/{paymentKey}")
    Payment findPaymentByPaymentKey(
            @PathVariable("paymentKey") String paymentKey
    );

    @GetMapping("/v1/promotions/card")
    CardPromotion getCardPromotionInfo();

    @PostMapping("/v1/payments/confirm")
    Payment approvePayment(@RequestBody PaymentApproveForm form);

    @PostMapping("{paymentId}/cancel")
    Payment cancelPayment(
            @PathVariable("paymentId") String paymentId,
            @RequestBody PaymentCancelForm form
    );

}
