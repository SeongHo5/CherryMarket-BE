package com.cherrydev.cherrymarketbe.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentCancel {

    //결제 취소 이력이 담기는 클래스

    private Number cancelAmount;  //결제를 취소한 금액
    private String cancelReason;  //결제를 취소한 이유
    private Number taxFreeAmount;  //취소된 금액 중 면세 금액
    private Integer taxExemptionAmount;  //취소된 금액 중 과세 제외 금액(컵 보증금 등)
    private Number refundableAmount;    // 결제 취소 후 환불 가능한 잔액
    private Number easyPayDiscountAmount; //간편결제 서비스의 포인트, 쿠폰, 즉시할인과 같은 적립식 결제수단에서 취소된 금액입니다.
    private String canceledAt; //결제 취소가 일어난 날짜와 시간 정보
    private String transactionKey; //취소 건의 키 값입니다. 여러 건의 취소 거래를 구분하는 데 사용
    private String receiptKey; //@Notnull, 취소 건의 현금영수증 키 값. 최대 길이는 200자



}
