package com.cherrydev.cherrymarketbe.server.application.customer.service;

import com.cherrydev.cherrymarketbe.server.application.account.service.AccountService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddReward;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.RewardInfo;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerReward;
import com.cherrydev.cherrymarketbe.server.domain.customer.enums.RewardGrantType;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.CustomerRewardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardService {

    private final AccountService accountService;
    private final CustomerRewardMapper customerRewardMapper;

    @Transactional
    public void grantReward(
            final RequestAddReward addRewardRequestDto
    ) {
        Account account = accountService.findAccountByEmail(addRewardRequestDto.getEmail());

        customerRewardMapper.save(addRewardRequestDto.toEntity(account));
    }

    @Transactional(readOnly = true)
    public RewardInfo getRewardSummaryAndList(
            final AccountDetails accountDetails
    ) {
        Account account = accountService.findAccountByEmail(accountDetails.getUsername());
        List<CustomerReward> rewards = customerRewardMapper.findAllByAccount(account);

        List<RewardInfo.RewardItem> rewardItems = rewards.stream()
                .map(this::generateRewardItem)
                .toList();

        RewardInfo.RewardSummary summary = generateRewardSummary(rewards);

        return RewardInfo.builder()
                .summary(summary)
                .rewards(rewardItems)
                .build();
    }

    private RewardInfo.RewardSummary generateRewardSummary(final List<CustomerReward> rewards) {
        Integer totalReward = rewards.stream()
                .filter(reward -> reward.getRewardGrantType() != RewardGrantType.USE)
                .mapToInt(CustomerReward::getAmounts)
                .sum();

        Integer usedReward = rewards.stream()
                .filter(reward -> reward.getRewardGrantType() == RewardGrantType.USE)
                .mapToInt(CustomerReward::getAmounts)
                .sum();

        Integer expiredReward = rewards.stream()
                .filter(credit -> !credit.getIsUsed() && credit.getExpiredAt().isBefore(LocalDate.now()))
                .filter(reward -> reward.getRewardGrantType() != RewardGrantType.USE)
                .mapToInt(CustomerReward::getAmounts)
                .sum();

        Integer availabledReward = totalReward - usedReward - expiredReward;

        return RewardInfo.RewardSummary.builder()
                .totalRewards(totalReward)
                .availableRewards(availabledReward)
                .usedRewards(usedReward)
                .expiredRewards(expiredReward)
                .build();
    }

    private RewardInfo.RewardItem generateRewardItem(final CustomerReward reward) {
        return RewardInfo.RewardItem.builder()
                .rewardGrantType(reward.getRewardGrantType().toString())
                .amounts(reward.getAmounts())
                .earnedAt(reward.getEarnedAt().toString())
                .expiredAt(reward.getExpiredAt().toString())
                .isUsed(reward.getIsUsed())
                .usedAt(reward.getUsedAt() == null ? null : reward.getUsedAt().toString())
                .build();
    }
}
