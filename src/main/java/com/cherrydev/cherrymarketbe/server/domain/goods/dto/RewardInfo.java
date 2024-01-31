package com.cherrydev.cherrymarketbe.server.domain.goods.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RewardInfo {

    List<RewardItemDto> rewards;
    RewardSummaryDto summary;

    @Getter
    @Builder
    public static class RewardItemDto {
        String rewardGrantType;
        Integer amounts;
        String earnedAt;
        String expiredAt;
        Boolean isUsed;
        String usedAt;
    }

    @Getter
    @Builder
    public static class RewardSummaryDto {
        Integer totalRewards;
        Integer usedRewards;
        Integer availableRewards;
        Integer expiredRewards;
    }

}
