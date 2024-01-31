package com.cherrydev.cherrymarketbe.server.domain.customer.entity;

import com.cherrydev.cherrymarketbe.server.domain.customer.enums.RewardGrantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CustomerReward {

    private Long rewardId;

    private Long accountId;

    private RewardGrantType rewardGrantType;

    private Integer amounts;

    private LocalDate earnedAt;

    private LocalDate expiredAt;

    private Boolean isUsed;

    private LocalDate usedAt;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Builder
    public CustomerReward(Long accountId, String rewardGrantType, Integer amounts,
                          LocalDate earnedAt, LocalDate expiredAt, Boolean isUsed, LocalDate usedAt) {
        this.accountId = accountId;
        this.rewardGrantType = RewardGrantType.valueOf(rewardGrantType);
        this.amounts = amounts;
        this.earnedAt = earnedAt;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
        this.usedAt = usedAt;
    }
}
