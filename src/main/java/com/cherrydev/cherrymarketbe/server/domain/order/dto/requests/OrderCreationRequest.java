package com.cherrydev.cherrymarketbe.server.domain.order.dto.requests;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.ProductDetails;
import lombok.Builder;

@Builder
public record OrderCreationRequest() {

    public ProductDetails create(AccountDetails accountDetails, GoodsInfo goodsInfo, Long orderId) {

        return ProductDetails.builder()
                .accountId(accountDetails.getAccount().getAccountId())
                .orderId(orderId)
                .goodsId(goodsInfo.goodsId())
                .quantity(goodsInfo.quantity())
                .build();
    }

}
