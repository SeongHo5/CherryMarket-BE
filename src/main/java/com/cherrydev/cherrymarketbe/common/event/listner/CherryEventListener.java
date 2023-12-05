package com.cherrydev.cherrymarketbe.common.event.listner;

import com.cherrydev.cherrymarketbe.common.event.AccountRegistrationEvent;
import com.cherrydev.cherrymarketbe.common.event.PasswordResetEvent;
import com.cherrydev.cherrymarketbe.common.service.EmailService;
import com.cherrydev.cherrymarketbe.customer.dto.reward.AddRewardRequestDto;
import com.cherrydev.cherrymarketbe.customer.service.RewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


import static java.time.OffsetDateTime.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class CherryEventListener {

    private final RewardService rewardService;
    private final EmailService emailService;

    @EventListener
    public void onAccountRegistration(AccountRegistrationEvent event) {
        log.info("Reward granted to {}", event.getAccount().getEmail());
        rewardService.grantReward(
                AddRewardRequestDto.builder()
                        .email(event.getAccount().getEmail())
                        .rewardGrantType(event.getRewardGrantType())
                        .amounts(event.getAmounts())
                        .earnedAt(now().toLocalDate().toString())
                        .expiredAt(now().plusYears(1).toLocalDate().toString())
                        .build()
        );
    }

    @EventListener
    public void onPasswordReset(PasswordResetEvent event) {
        log.info("Reset password for {}", event.getEmail());
        emailService.sendNotificationMail(event.getEmail());
    }
}
