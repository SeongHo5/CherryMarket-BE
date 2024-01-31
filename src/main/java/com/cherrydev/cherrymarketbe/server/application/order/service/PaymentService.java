package com.cherrydev.cherrymarketbe.server.application.order.service;

import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.PaymentDetailsInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.PaymentSummary;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.requests.PaymentDetailRequest;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.PaymentDetails;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.payment.PaymentMapper;
import com.cherrydev.cherrymarketbe.server.domain.payment.model.payment.Payment;
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
