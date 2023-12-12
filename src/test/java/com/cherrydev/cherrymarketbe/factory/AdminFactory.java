package com.cherrydev.cherrymarketbe.factory;

import com.cherrydev.cherrymarketbe.admin.dto.IssueCouponDto;
import com.cherrydev.cherrymarketbe.admin.dto.ModifyUserRoleDto;
import com.cherrydev.cherrymarketbe.admin.dto.ModifyUserStatusDto;
import com.cherrydev.cherrymarketbe.customer.dto.reward.AddRewardRequestDto;

public class AdminFactory {

    public static ModifyUserRoleDto createModifyUserRoleRequestDtoA() {
        return ModifyUserRoleDto.builder()
                .email("jeongnamgim@example.org")
                .newRole("ROLE_SELLER")
                .build();
    }

    public static ModifyUserStatusDto createModifyUserStatusByAdminDtoA() {
        return ModifyUserStatusDto.builder()
                .email("kgim@example.net")
                .newStatus("RESTRICTED")
                .restrictedUntil("2025-12-31")
                .build();
    }

    public static ModifyUserStatusDto createModifyUserStatusByAdminDtoB() {
        return ModifyUserStatusDto.builder()
                .email("kgim@example.net")
                .newStatus("RESTRICTED")
                .build();
    }

    public static ModifyUserStatusDto createModifyUserStatusByAdminDtoC() {
        return ModifyUserStatusDto.builder()
                .email("kgim@example.net")
                .newStatus("RESTRICTED")
                .restrictedUntil("2020-12-31")
                .build();
    }

    public static AddRewardRequestDto createAddRewardRequestDtoA() {
        return AddRewardRequestDto.builder()
                .email("sungho5527@naver.com")
                .rewardGrantType("ADMIN")
                .amounts(1000)
                .earnedAt("2023-01-01")
                .expiredAt("2024-12-31")
                .build();
    }

    public static AddRewardRequestDto createAddRewardRequestDtoB() {
        return AddRewardRequestDto.builder()
                .email("nothing1515@example.com")
                .rewardGrantType("ADMIN")
                .amounts(1000)
                .earnedAt("2023-01-01")
                .expiredAt("2024-12-31")
                .build();
    }

    public static IssueCouponDto createIssueCouponRequestDtoA() {
        return IssueCouponDto.builder()
                .code("TEST00")
                .type("OTHER")
                .minimumOrderAmount(10000)
                .discountRate(10)
                .startDate("2021-01-01")
                .endDate("2024-12-31")
                .build();
    }

    public static IssueCouponDto createIssueCouponRequestDtoB() {
        return IssueCouponDto.builder()
                .code("WELCO0ME")
                .type("OTHER")
                .minimumOrderAmount(10000)
                .discountRate(10)
                .startDate("2021-01-01")
                .endDate("2024-12-31")
                .build();
    }
}
