package com.cherrydev.cherrymarketbe.order.domain;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public record ConsumerInfo(
        String name,
        String contact,

        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:SS")
        Timestamp approveAt
        ) {

    public static ConsumerInfo getInfo(Order order, AccountDetails accountDetails) {

        return new ConsumerInfo(
                accountDetails.getAccount().getName(),
                accountDetails.getAccount().getContact(),
                order.getCreatedAt()
        );
    }


}
