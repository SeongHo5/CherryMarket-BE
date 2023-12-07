package com.cherrydev.cherrymarketbe.goods.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

import java.sql.Date;

@Value
public class GoodsRegistrationDto {


    Long goodsId;

    @NotNull
    Long makerId;

    @NotNull
    Long categoryId;

    @Pattern(regexp = "\\d{7}", message = "상품 코드는 7자리 정수여야 합니다.")
    String goodsCode;

    @NotNull
    String goodsName;

    @NotNull
    String description;

    Long discountId;

    @NotNull
    int price;

    @NotNull
    int retailPrice;

    @NotNull
    int inventory;

    @Pattern(regexp = "^(REFRIGERATION|FROZEN|ROOM_TEMPERATURE)$", message = "보관 타입 형식에 맞지 않습니다")
    @NotNull
    String storageType;

    @NotNull
    String capacity;

    @NotNull
    String expDate;

    String allergyInfo;

    @NotNull
    String originPlace;

    @Pattern(regexp = "^(ON_SALE|PAUSE|DISCONTINUANCE)$", message = "상품 판매 상태 형식에 맞지 않습니다")
    @NotNull
    String salesStatus;

    Date createDate;

    Date updateDate;
}
