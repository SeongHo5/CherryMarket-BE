package com.cherrydev.cherrymarketbe.customer.dto.reward;

import com.cherrydev.cherrymarketbe.customer.enums.RewardGrantType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RewardInfoDto {

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
