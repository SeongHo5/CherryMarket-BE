package com.cherrydev.cherrymarketbe.payment.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentDetail {


    private String version;    //Payment 객체의 응답 버전

    private  String paymentKey;    //결제의 키 값

    private String type;    //결제 타입 정보

    private String orderId;    //주문 ID

    private String orderName;    //주문명

    private String mId;    //상점 아이디(MID) - 토스페이먼츠에서 발급;

    private String currency;    //결제할 때 사용한 통화

    private String method;    //결제수단 (ex, 카드, 간편결제, 휴대폰 등)

    private Number totalAmount;    //총 결제금액

    private Number balanceAmount;    //취소할 수 있는 금액(잔고)

    private String status;    //결제 처리상태  (필요시 enum 사용)

    private String requestedAt;    //결제가 일어난 날짜와 시간 정보

    private String approvedAt;    //결제 승인이 일어난 날짜와 시간 정보

    private boolean useEscrow;      //에스크로 사용 여부

    private String lastTransactionKey;      //마지막 거래의 키 값. @NotNull

    private Number suppliedAmount;     //공급가액

    private Number vat;//부가세

    private List<PaymentCancel> cancels;

    private boolean isPartialCancelable;

    private Card card;

    private VirtualAccount virtualAccount;

    private String secret;





























}
