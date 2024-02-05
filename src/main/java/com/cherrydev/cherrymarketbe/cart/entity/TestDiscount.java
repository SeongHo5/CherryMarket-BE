package com.cherrydev.cherrymarketbe.cart.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TestDiscount {

    private Long discountId;
    private String discountType;
    private int discountRate;

    @Builder
    public TestDiscount(Long discountId, String discountType, int discountRate) {
        this.discountId = discountId;
        this.discountType = discountType;
        this.discountRate = discountRate;
    }
}
