package com.cherrydev.cherrymarketbe.admin.entity;

import com.cherrydev.cherrymarketbe.admin.enums.CouponType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Coupon {

    Long id;

    @Pattern(regexp = "^[A-Z0-9]{8}$", message = "쿠폰 코드는 8자리의 영문 대문자와 숫자로 이루어져야 합니다.")
    String code;

    CouponType type;

    Integer minimumOrderAmount;

    Integer discountAmount;

    @PastOrPresent
    LocalDate startDate;

    @Future
    LocalDate endDate;

    Timestamp createdAt;

    Timestamp updatedAt;


    @Builder
    public Coupon(String code, CouponType type, Integer minimumOrderAmount, Integer discountAmount, LocalDate startDate, LocalDate endDate) {
        this.code = code;
        this.type = type;
        this.minimumOrderAmount = minimumOrderAmount;
        this.discountAmount = discountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
