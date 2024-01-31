package com.cherrydev.cherrymarketbe.server.application.order.event;

import com.cherrydev.cherrymarketbe.server.domain.payment.model.payment.Payment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderCancelEvent extends ApplicationEvent {

    private final String accountEmail;
    private final String orderCode;
    private final Payment payment;
    private final Integer rewardAmount;

    public OrderCancelEvent(Object source, String accountEmail, Integer rewardAmount, String orderCode, Payment payment) {
        super(source);
        this.accountEmail = accountEmail;
        this.rewardAmount = rewardAmount;
        this.orderCode = orderCode;
        this.payment = payment;
    }

}
