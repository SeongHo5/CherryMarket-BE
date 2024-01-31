package com.cherrydev.cherrymarketbe.server.application.payments.service;

import com.cherrydev.cherrymarketbe.server.domain.payment.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.dto.PaymentApproveForm;

import com.cherrydev.cherrymarketbe.server.domain.payment.model.cardpromotion.CardPromotion;
import com.cherrydev.cherrymarketbe.server.domain.payment.model.payment.Payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TossPaymentsService {

    private final TossFeignClient tossFeignClient;

    /**
     * 주문 번호로 결제 조회
     * @param orderCode 주문 번호
     */
    public Payment findPaymentByOrderId(String orderCode) {
        return tossFeignClient.findPaymentByOrderId(orderCode);
    }

    /**
     * 결제 고유 번호로 결제 조회
     * @param paymentKey 결제 고유 번호
     */
    public Payment findPaymentByPaymentKey(String paymentKey) {
        return tossFeignClient.findPaymentByPaymentKey(paymentKey);
    }

    /**
     * 결제 승인
     */
    @Transactional
    public Payment approvePayment(PaymentApproveForm form) {
        return tossFeignClient.approvePayment(form);
    }

    /**
     * 결제 취소
     * @param paymentKey 결제 고유 번호
     * @param form 취소 정보
     * @return Payment
     */
    @Transactional
    public Payment cancelPayment(String paymentKey, PaymentCancelForm form) {
        return tossFeignClient.cancelPayment(paymentKey, form);
    }

    /**
     * 카드 혜택 조회
     */
    public CardPromotion getCardPromotionInfo() {
        return tossFeignClient.getCardPromotionInfo();
    }

}
