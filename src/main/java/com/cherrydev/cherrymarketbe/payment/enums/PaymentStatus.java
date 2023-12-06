package com.cherrydev.cherrymarketbe.payment.enums;

public enum PaymentStatus {


    READY,  //결제를 생성 후 초기 상태. 인증 전까지 유지
    IN_PROGRESS, //결제수단 정보와 해당 결제수단의 소유자가 맞는지 인증을 마친 상태입니다. 결제 승인 API를 호출하면 결제가 완료.
    WAITING_FOR_DEPOSIT, //가상계좌 관련
    DONE, //인증된 결제수단 정보, 고객 정보로 요청한 결제가 승인된 상태
    CANCELED, //승인된 결제가 취소된 상태
    PARTIAL_CANCELED, //승인된 결제가 부분 취소된 상태
    ABORTED, //결제 승인이 실패한 상태
    EXPIRED //결제 유효 시간 30분이 지나 거래가 취소된 상태. or  IN_PROGRESS 상태에서 결제 승인 API를 호출하지 않으면 EXPIRED가 됩니다.

}
