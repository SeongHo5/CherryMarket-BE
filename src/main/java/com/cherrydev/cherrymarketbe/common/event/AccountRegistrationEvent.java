package com.cherrydev.cherrymarketbe.common.event;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AccountRegistrationEvent extends ApplicationEvent {

    public static final String SIGN_UP_REWARD_TYPE = "WELCOME";
    private static final int WELCOME_REWARD_AMOUNTS = 1000;

    private final Account account;
    private final int amounts;
    private final String rewardGrantType;


    public AccountRegistrationEvent(Object source, Account account) {
        super(source);
        this.account = account;
        this.amounts = WELCOME_REWARD_AMOUNTS;
        this.rewardGrantType = SIGN_UP_REWARD_TYPE;
    }
}
