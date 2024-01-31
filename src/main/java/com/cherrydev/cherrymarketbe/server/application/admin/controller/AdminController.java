package com.cherrydev.cherrymarketbe.server.application.admin.controller;

import com.cherrydev.cherrymarketbe.server.application.admin.service.AdminService;
import com.cherrydev.cherrymarketbe.server.application.admin.service.CouponService;
import com.cherrydev.cherrymarketbe.server.application.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.server.application.customer.service.RewardService;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.IssueCoupon;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserRole;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.ModifyUserStatus;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.AdminUserInfo;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.CouponInfo;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddReward;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final CouponService couponService;
    private final RewardService rewardService;

    /**
     * 계정 목록 전체 조회
     * @param pageable 페이징 정보(page, size)
     */
    @GetMapping("/account-list")
    public ResponseEntity<MyPage<AdminUserInfo>> searchAccounts(
            final Pageable pageable
    ) {
        return adminService.getAllAcounts(pageable);
    }

    /**
     * 계정 권한 변경
     */
    @PatchMapping("/modify/account/role")
    @ResponseStatus(HttpStatus.OK)
    public void modifyAccountRole(
            final @RequestBody ModifyUserRole roleRequestDto
    ) {
        adminService.modifyAccountRole(roleRequestDto);
    }

    /**
     * 계정 상태 변경
     */
    @PatchMapping("/modify/account/status")
    @ResponseStatus(HttpStatus.OK)
    public void modifyAccountStatus(
            final @RequestBody ModifyUserStatus statusRequestDto
    ) {
        adminService.modifyAccountStatus(statusRequestDto);
    }

    /**
     * 관리자에 의해 포인트 지급
     * @param addRewardRequestDto 지급할 포인트 정보
     */
    @PostMapping("/grant-reward")
    @ResponseStatus(HttpStatus.OK)
    public void grantReward(
            final @RequestBody RequestAddReward addRewardRequestDto
    ) {
        rewardService.grantReward(addRewardRequestDto);
    }

    /**
     * 관리자에 의해 쿠폰 발행
     * @param issueCoupon 발행할 쿠폰 정보
     */
    @PostMapping("/issue-coupon")
    @ResponseStatus(HttpStatus.OK)
    public void issueCoupon(
            final @RequestBody IssueCoupon issueCoupon
    ) {
        couponService.issueCoupon(issueCoupon);
    }

    /**
     * 쿠폰 목록 전체 조회
     * @param pageable 페이징 정보(page, size)
     * @return 쿠폰 목록
     */
    @GetMapping("/coupon-list")
    public ResponseEntity<MyPage<CouponInfo>> searchCoupons(
            final Pageable pageable
    ) {
        return couponService.getAllCoupons(pageable);
    }

}
