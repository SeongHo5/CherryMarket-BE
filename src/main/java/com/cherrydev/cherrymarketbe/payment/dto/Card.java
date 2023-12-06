package com.cherrydev.cherrymarketbe.payment.dto;

import lombok.Builder;

@Builder
public class Card {

    private Number amount; //카드사에 결제 요청한 금액. 즉시 할인 금액(discount.amount)이 포함됩니다.
    private String issuerCode;//카드 발급사 숫자 코드입니다. 카드사 코드를 참고하세요.
    private String acquirerCode; //카드 매입사 숫자 코드입니다. 카드사 코드를 참고하세요.
    private String number; //카드번호입니다. 번호의 일부는 마스킹 되어 있습니다. 최대 길이는 20자입니다.
    private Integer installmentPlanMonths; //할부 개월 수입니다. 일시불이면 0입니다.
    private String approveNo; //카드사 승인 번호입니다. 최대 길이는 8자입니다.
    private boolean useCardPoint; //카드사 포인트 사용 여부입니다.
    private String cardType; //카드 종류입니다. 신용, 체크, 기프트, 미확인 중 하나입니다.
    private String ownerType; //카드의 소유자 타입. 개인, 법인, 미확인 중 하나.
    private String acquireStatus; //카드 결제의 매입 상태. READY, REQUESTED, COMPLETED, CANCEL_REQUESTED, CANCELED
    private boolean isInterestFree; //무이자 할부의 적용 여부
    private String interestPayer; //할부가 적용된 결제에서 할부 수수료를 부담하는 주체. BUYER, CARD_COMPANY, MERCHANT 중 하나입니다.

}
