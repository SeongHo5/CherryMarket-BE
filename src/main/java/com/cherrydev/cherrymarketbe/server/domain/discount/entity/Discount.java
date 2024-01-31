package com.cherrydev.cherrymarketbe.server.domain.discount.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Discount {

    private Long discountId;
    private String discountType;
    private int discountRate;

    @Builder
    public Discount(Long discountId, String discountType, int discountRate) {
        this.discountId = discountId;
        this.discountType = discountType;
        this.discountRate = discountRate;
    }
}
