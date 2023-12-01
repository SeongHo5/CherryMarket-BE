package com.cherrydev.cherrymarketbe.customer.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.service.impl.AccountServiceImpl;
import com.cherrydev.cherrymarketbe.customer.dto.reward.AddRewardRequestDto;
import com.cherrydev.cherrymarketbe.customer.dto.reward.RewardInfoDto;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerReward;
import com.cherrydev.cherrymarketbe.customer.repository.RewardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardService {

    private final AccountServiceImpl accountService;
    private final RewardMapper rewardMapper;

    @Transactional
    public void grantReward(
            final AddRewardRequestDto addRewardRequestDto
    ) {
        Account account = accountService.findAccountByEmail(addRewardRequestDto.getEmail());

        rewardMapper.save(addRewardRequestDto.toEntity(account));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<RewardInfoDto> getRewardSummaryAndList(
            final AccountDetails accountDetails
    ) {
        Account account = accountService.findAccountByEmail(accountDetails.getUsername());
        List<CustomerReward> rewards = rewardMapper.findAllByAccount(account);

        List<RewardInfoDto.RewardItemDto> rewardItems = rewards.stream()
                .map(this::generateRewardItem)
                .toList();

        RewardInfoDto.RewardSummaryDto summary = generateRewardSummary(rewards);

        return ResponseEntity.ok(RewardInfoDto.builder()
                .rewards(rewardItems)
                .summary(summary)
                .build());
    }

    private RewardInfoDto.RewardSummaryDto generateRewardSummary(
            final List<CustomerReward> rewards
    ) {
        Integer totalReward = rewards.stream()
                .mapToInt(CustomerReward::getAmounts)
                .sum();

        Integer availabledReward = rewards.stream()
                .filter(credit -> !credit.getIsUsed() && credit.getExpiredAt().isAfter(LocalDate.now()))
                .mapToInt(CustomerReward::getAmounts)
                .sum();

        Integer usedReward = rewards.stream()
                .filter(CustomerReward::getIsUsed)
                .mapToInt(CustomerReward::getAmounts)
                .sum();

        Integer expiredReward = rewards.stream()
                .filter(credit -> !credit.getIsUsed() && credit.getExpiredAt().isBefore(LocalDate.now()))
                .mapToInt(CustomerReward::getAmounts)
                .sum();

        return RewardInfoDto.RewardSummaryDto.builder()
                .totalRewards(totalReward)
                .availableRewards(availabledReward)
                .usedRewards(usedReward)
                .expiredRewards(expiredReward)
                .build();
    }

    private RewardInfoDto.RewardItemDto generateRewardItem(
            final CustomerReward reward
    ) {
        return RewardInfoDto.RewardItemDto.builder()
                .rewardGrantType(reward.getRewardGrantType().toString())
                .amounts(reward.getAmounts())
                .earnedAt(reward.getEarnedAt().toString())
                .expiredAt(reward.getExpiredAt().toString())
                .isUsed(reward.getIsUsed())
                .usedAt(reward.getUsedAt() == null ? null : reward.getUsedAt().toString())
                .build();
    }
}
