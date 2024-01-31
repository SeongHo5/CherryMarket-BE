package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Order;
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
