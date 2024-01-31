package com.cherrydev.cherrymarketbe.server.domain.admin.dto.request;

import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Coupon;
import com.cherrydev.cherrymarketbe.server.domain.admin.enums.CouponType;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class IssueCoupon {

    @Pattern(regexp = "^[A-Z0-9]{8}$", message = "쿠폰 코드는 8자리의 영문 대문자와 숫자로 이루어져야 합니다.")
    String code;

    String type;

    Integer minimumOrderAmount;

    @Pattern(regexp = "^\\d{1,2}$", message = "할인율은 1 ~ 99 사이의 숫자로 입력해주세요.")
    Integer discountRate;

    String startDate;

    String endDate;

    public Coupon toEntity() {
        return Coupon.builder()
                .code(code)
                .type(CouponType.valueOf(type))
                .minimumOrderAmount(minimumOrderAmount)
                .discountAmount(discountRate)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .build();
    }
}
