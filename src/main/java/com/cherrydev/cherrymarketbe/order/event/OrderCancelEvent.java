package com.cherrydev.cherrymarketbe.order.event;

import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderCancelEvent extends ApplicationEvent {

    private final String orderCode;
    private final Payment payment;

    public OrderCancelEvent(Object source, String orderCode, Payment payment) {
        super(source);
        this.orderCode = orderCode;
        this.payment = payment;
    }

}
