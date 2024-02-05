package com.cherrydev.cherrymarketbe.admin.dto;

import com.cherrydev.cherrymarketbe.admin.enums.CouponType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CouponInfoDto {

    String code;

    CouponType type;

    Integer minimumOrderAmount;

    Integer discountAmount;

    String startDate;

    String endDate;
}
