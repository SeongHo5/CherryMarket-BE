package com.cherrydev.cherrymarketbe.order.enums;

import lombok.Getter;

@Getter
public enum ShippingStatus {

    ORDER_RECEIVED("주문 접수"),
    PREPARING_FOR_SHIPMENT("배송 준비 중"),
    IN_TRANSIT("배송 중"),
    DELIVERED("배송 완료"),
    CANCELED("배송 취소");

    private final String status;

    ShippingStatus(String status){
        this.status = status;
    }

}

