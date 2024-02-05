package com.cherrydev.cherrymarketbe.order.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.domain.PaymentDetailsInfo;
import com.cherrydev.cherrymarketbe.order.domain.PaymentSummary;
import com.cherrydev.cherrymarketbe.order.dto.requests.PaymentDetailRequest;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.order.entity.PaymentDetails;
import com.cherrydev.cherrymarketbe.order.repository.PaymentMapper;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMapper paymentMapper;


    @Transactional
    public ResponseEntity<PaymentDetailsInfo> findPaymentDetailsByOrderCode(String orderCode) {
        return ResponseEntity.ok(paymentMapper.findByOrderCode(orderCode));
    }

    @Transactional
    public void createPaymentDetail(
            AccountDetails accountDetails,
            OrderReceiptResponse requestDto,
            Payment getOrderWithPayment,
            Long orderId
    ) {
        PaymentDetails paymentDetails = new PaymentDetailRequest()
                .create(
                        accountDetails,
                        requestDto,
                        getOrderWithPayment,
                        orderId
                );
        paymentMapper.save(paymentDetails);
    }

    @Transactional
    public void cancelPaymentDetail(String orderCode, Payment cancelPayment) {
        PaymentDetailsInfo existingPaymentInfo = getOrderWithPaymentDetail(orderCode);
        PaymentDetails cancelPaymentInfoCreation = new PaymentSummary().update(
                        orderCode,
                        cancelPayment,
                        existingPaymentInfo
                );
       paymentMapper.saveCancelData(cancelPaymentInfoCreation);
    }

    public PaymentDetailsInfo getOrderWithPaymentDetail(String orderCode) {
        return  paymentMapper.getByOrderCode(orderCode);
    }


}
