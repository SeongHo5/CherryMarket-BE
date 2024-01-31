package com.cherrydev.cherrymarketbe.server.domain.customer.entity;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class CustomerCoupon {

    Long couponId;

    Long accountId;

    Boolean isUsed;

    Timestamp usedAt;

    Long orderId;

    Timestamp createdAt;

    Timestamp updatedAt;
}
