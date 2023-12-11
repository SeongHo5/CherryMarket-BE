package com.cherrydev.cherrymarketbe.goods.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoodsExceptionStatus {
    DISCONTINUED_GOODS(400, "해당 상품은 단종 되었습니다"),
    ON_SALE_GOODS(400, "판매중인 상품은 삭제할 수 없습니다"),
    INVALID_BUSINESS_NUMBER_FORMAT(400, "사업자 번호 형식이 유효하지 않습니다"),


    GOODS_NOT_FOUND(404, "해당 상품이 존재하지 않습니다"),
    MAKER_NOT_FOUND(404, "해당 제조사는 존재하지 않습니다");

    private final int statusCode;
    private final String message;
}
