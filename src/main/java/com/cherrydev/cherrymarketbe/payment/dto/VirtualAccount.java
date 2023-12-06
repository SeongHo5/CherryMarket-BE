package com.cherrydev.cherrymarketbe.payment.dto;

public class VirtualAccount {

    private String accountType; //가상계좌 타입

    private String accountNumber; //발급된 계좌번호입니다. 최대 길이는 20자입니다.

    private String bankCode; //가상계좌 은행 숫자 코드입니다. 은행 코드와 증권사 코드를 참고하세요.

    private String customerName; //가상계좌를 발급한 고객 이름입니다. 최대 길이는 100자입니다.

    private String dueDate; //입금 기한입니다. yyyy-MM-dd'T'HH:mm:ss ISO 8601 형식을 사용합니다.

    private String refundStatus; //환불 처리 상태입니다.  [NONE, PENDING, FAILED, PARTIAL_FAILED, COMPLETED]

    private boolean expired; //가상계좌의 만료 여부입니다.

    private String settlementStatus; //정산 상태입니다. 정산이 아직 되지 않았다면 INCOMPLETED, 정산이 완료됐다면 COMPLETED 값이 들어옵니다.

    private RefundReceiveAccount refundReceiveAccount; //결제위젯 가상계좌 환불 정보 입력 기능으로 받은 고객의 환불 계좌 정보입니다. 은행 코드(bankCode), 계좌번호(accountNumber), 예금주 정보(holderName)가 담긴 객체입니다.





}
