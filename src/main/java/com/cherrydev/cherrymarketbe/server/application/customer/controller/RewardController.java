package com.cherrydev.cherrymarketbe.server.application.customer.controller;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.RewardInfo;
import com.cherrydev.cherrymarketbe.server.application.customer.service.RewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/reward")
public class RewardController {

    private final RewardService rewardService;

    /**
     * 내 리워드 정보 조회
     * @param accountDetails 로그인한 회원 정보
     * @return 리워드 내역 및 요약 정보
     */
    @GetMapping("/summary")
    public ResponseEntity<RewardInfo> getRewardInfo(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        RewardInfo response = rewardService.getRewardSummaryAndList(accountDetails);
        return ResponseEntity.ok(response);
    }
}
