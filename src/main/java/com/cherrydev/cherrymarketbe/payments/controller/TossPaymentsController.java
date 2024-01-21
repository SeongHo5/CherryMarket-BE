package com.cherrydev.cherrymarketbe.payments.controller;

import com.cherrydev.cherrymarketbe.payments.model.cardpromotion.CardPromotion;
import com.cherrydev.cherrymarketbe.payments.service.TossPaymentsService;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentApproveForm;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class TossPaymentsController {

    private final TossPaymentsService tossPaymentsService;

    @GetMapping ("/find/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public Payment getPaymentByOrderId(
            final @PathVariable String orderId
    ) {
        return tossPaymentsService.findPaymentByOrderId(orderId);
    }

    @GetMapping ("/find/{paymentKey}")
    @ResponseStatus(HttpStatus.OK)
    public Payment getPaymentByPaymentKey(
            final @PathVariable String paymentKey
    ) {
        return tossPaymentsService.findPaymentByPaymentKey(paymentKey);
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public Payment confirmPayment(
            final @RequestBody PaymentApproveForm form
            ) {
        return tossPaymentsService.approvePayment(form);
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    public Payment cancelPayment(
            final @RequestParam String paymentKey,
            final @RequestBody PaymentCancelForm form
    ) {
        return tossPaymentsService.cancelPayment(paymentKey, form);
    }

    @GetMapping("/card-promotion")
    @ResponseStatus(HttpStatus.OK)
    public CardPromotion getCardPromotion(){
        return tossPaymentsService.getCardPromotionInfo();
    }


}
