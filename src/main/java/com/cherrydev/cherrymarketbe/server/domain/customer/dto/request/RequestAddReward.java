package com.cherrydev.cherrymarketbe.server.domain.customer.dto.request;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerReward;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class RequestAddReward {

    @Email(message = "이메일 형식이 맞지 않습니다.")
    String email;

    @Pattern(regexp = "^(SIGN_UP|PURCHASE|REVIEW|EVENT|ADMIN|USE)$", message = "리워드 지급 타입이 맞지 않습니다.")
    String rewardGrantType;

    @Pattern(regexp = "^\\d+$", message = "숫자 형식이 맞지 않습니다.")
    Integer amounts;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식이 맞지 않습니다.")
    String earnedAt;

    @Future
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식이 맞지 않습니다.")
    String expiredAt;

    public CustomerReward toEntity(Account account) {
        return CustomerReward.builder()
                .accountId(account.getAccountId())
                .rewardGrantType(this.rewardGrantType)
                .amounts(this.amounts)
                .earnedAt(LocalDate.parse(earnedAt))
                .expiredAt(LocalDate.parse(expiredAt))
                .isUsed(false)
                .build();
    }

}
