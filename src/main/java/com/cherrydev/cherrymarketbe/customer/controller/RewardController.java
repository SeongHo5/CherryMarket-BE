package com.cherrydev.cherrymarketbe.customer.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.customer.dto.reward.RewardInfoDto;
import com.cherrydev.cherrymarketbe.customer.service.RewardService;
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

    @GetMapping("/summary")
    public ResponseEntity<RewardInfoDto> getRewardInfo(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return rewardService.getRewardSummaryAndList(accountDetails);
    }
}
