package com.cherrydev.cherrymarketbe.admin.dto;

import lombok.Builder;

@Builder
public record GrantCouponByAdminDto(String email, String couponCode, String expiredAt) {

}
