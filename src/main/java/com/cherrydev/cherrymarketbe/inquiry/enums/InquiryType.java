package com.cherrydev.cherrymarketbe.inquiry.enums;

import lombok.Getter;

@Getter
public enum InquiryType {

    CANCEL("취소/교환/반품", InquiryDetailType.CANCEL, InquiryDetailType.EXCHANGE, InquiryDetailType.REFUND),
    MEMBER("회원/이벤트/쿠폰", InquiryDetailType.COUPON, InquiryDetailType.POINT, InquiryDetailType.MEMBER_INFO, InquiryDetailType.EVENT, InquiryDetailType.SIGN_UP),
    GOODS("상품(식품/비식품/티켓)", InquiryDetailType.PRODUCT_QUALITY, InquiryDetailType.FOREIGN_SUBSTANCE, InquiryDetailType.PRODUCT_INFO),
    DELIVERY("배송", InquiryDetailType.DIFFERENT_PRODUCT_RECEIPT, InquiryDetailType.UNRECEIVED_PRODUCT, InquiryDetailType.DELIVERY_IMPROVEMENT, InquiryDetailType.DELIVERY_SCHEDULE_INFO),
    ORDER("주문/결제", InquiryDetailType.INFO_CHANGE, InquiryDetailType.ORDER_HISTORY_RECEIPT, InquiryDetailType.ORDER_PAYMENT_METHOD),
    SERVICE("서비스/오류/기타", InquiryDetailType.SYSTEM_ERROR, InquiryDetailType.OTHER, InquiryDetailType.SERVICE_SUGGESTION);

    private final String type;
    private final InquiryDetailType[] allowedDetailTypes;

    InquiryType(String type, InquiryDetailType... allowedDetailTypes) {
        this.type = type;
        this.allowedDetailTypes = allowedDetailTypes;
    }
}
