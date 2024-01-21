package com.cherrydev.cherrymarketbe.payments.service;

import com.cherrydev.cherrymarketbe.config.feign.TossFeignConfig;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentApproveForm;
import com.cherrydev.cherrymarketbe.payments.model.cardpromotion.CardPromotion;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "toss_payment", url = "https://api.tosspayments.com/v1/payments", configuration = TossFeignConfig.class)
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
