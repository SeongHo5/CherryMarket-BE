package com.cherrydev.cherrymarketbe.factory;

import com.cherrydev.cherrymarketbe.admin.dto.ModifyUserRoleRequestDto;
import com.cherrydev.cherrymarketbe.admin.dto.ModifyUserStatusByAdminDto;
import com.cherrydev.cherrymarketbe.customer.dto.reward.AddRewardRequestDto;

import java.time.LocalDate;

public class AdminFactory {

    public static ModifyUserRoleRequestDto createModifyUserRoleRequestDtoA() {
        return ModifyUserRoleRequestDto.builder()
                .email("jeongnamgim@example.org")
                .newRole("ROLE_SELLER")
                .build();
    }

    public static ModifyUserStatusByAdminDto createModifyUserStatusByAdminDtoA() {
        return ModifyUserStatusByAdminDto.builder()
                .email("kgim@example.net")
                .newStatus("RESTRICTED")
                .restrictedUntil("2025-12-31")
                .build();
    }

    public static ModifyUserStatusByAdminDto createModifyUserStatusByAdminDtoB() {
        return ModifyUserStatusByAdminDto.builder()
                .email("kgim@example.net")
                .newStatus("RESTRICTED")
                .build();
    }

    public static ModifyUserStatusByAdminDto createModifyUserStatusByAdminDtoC() {
        return ModifyUserStatusByAdminDto.builder()
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
}
