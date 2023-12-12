package com.cherrydev.cherrymarketbe.inquiry.enums;

import lombok.Getter;

@Getter
public enum InquiryDetailType {

    CANCEL("취소"),
    EXCHANGE("교환"),
    REFUND("반품"),
    COUPON("쿠폰/할인혜택"),
    POINT("적립금"),
    MEMBER_INFO("회원정보/등급"),
    EVENT("이벤트 내용/참여"),
    SIGN_UP("회원가입/탈퇴"),
    PRODUCT_QUALITY("상품품질"),
    FOREIGN_SUBSTANCE("상품 내 이물질"),
    PRODUCT_INFO("상품정보"),
    DIFFERENT_PRODUCT_RECEIPT("다른 상품 수령"),
    UNRECEIVED_PRODUCT("상품 미수령"),
    DELIVERY_IMPROVEMENT("배송/포장개선요청"),
    DELIVERY_SCHEDULE_INFO("배송일정/정보"),
    INFO_CHANGE("정보변경(주소/출입방법)"),
    ORDER_HISTORY_RECEIPT("주문내역/영수증 발급"),
    ORDER_PAYMENT_METHOD("주문/결제방법"),
    SYSTEM_ERROR("시스템 오류/장애"),
    OTHER("기타(직접입력)"),
    SERVICE_SUGGESTION("서비스 제안/개선");

    private final String detailType;

    InquiryDetailType(String detailType) {
        this.detailType = detailType;
    }
}
