package com.cherrydev.cherrymarketbe.server.application.account.event.listner;

import com.cherrydev.cherrymarketbe.server.application.account.event.AccountRegistrationEvent;
import com.cherrydev.cherrymarketbe.server.application.account.event.PasswordResetEvent;
import com.cherrydev.cherrymarketbe.server.application.admin.service.CouponService;
import com.cherrydev.cherrymarketbe.server.application.common.service.EmailService;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.GrantCouponByAdmin;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddReward;
import com.cherrydev.cherrymarketbe.server.application.customer.service.RewardService;
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
    private final CouponService couponService;
    private final EmailService emailService;

    @EventListener
    public void onAccountRegistration(AccountRegistrationEvent event) {

        rewardService.grantReward(
                RequestAddReward.builder()
                        .email(event.getAccount().getEmail())
                        .rewardGrantType(event.getRewardGrantType())
                        .amounts(event.getAmounts())
                        .earnedAt(now().toLocalDate().toString())
                        .expiredAt(now().plusYears(1).toLocalDate().toString())
                        .build()
        );

        couponService.grantCoupon(
                GrantCouponByAdmin.builder()
                        .email(event.getAccount().getEmail())
                        .couponCode(event.getCouponCode())
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
