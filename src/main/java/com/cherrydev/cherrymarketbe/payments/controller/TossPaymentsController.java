package com.cherrydev.cherrymarketbe.payments.controller;

import com.cherrydev.cherrymarketbe.payments.model.cardpromotion.CardPromotion;
import com.cherrydev.cherrymarketbe.payments.service.TossPaymentsService;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentConfirmForm;
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

    @GetMapping ("/find-orderId")
    @ResponseStatus(HttpStatus.OK)
    public Payment getPaymentByOrderId(
            @RequestParam String orderId
    ) {
        return tossPaymentsService.findPaymentByOrderId(orderId);
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public Payment confirmPayment(
            @RequestBody PaymentConfirmForm form
            ) {
        return tossPaymentsService.paymentConfirm(form);
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    public Payment cancelPayment(
            @RequestParam String paymentKey,
            @RequestBody PaymentCancelForm form
    ) {
        return tossPaymentsService.paymentCancel(paymentKey, form);
    }

    @GetMapping("/card-promotion")
    @ResponseStatus(HttpStatus.OK)
    public CardPromotion getCardPromotion(){
        return tossPaymentsService.cardPromotion();
    }


}