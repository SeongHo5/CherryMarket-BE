package com.cherrydev.cherrymarketbe.order.event;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderCreationEvent extends ApplicationEvent {

    private final AccountDetails accountDetails;
    private final OrderReceiptResponse requestDto;
    private final Payment payment;
    private final Long orderId;

    public OrderCreationEvent(Object source,
                              AccountDetails accountDetails,
                              OrderReceiptResponse requestDto,
                              Payment payment,
                              Long orderId) {
        super(source);
        this.accountDetails = accountDetails;
        this.requestDto = requestDto;
        this.payment = payment;
        this.orderId = orderId;
    }

}
