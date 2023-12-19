package com.cherrydev.cherrymarketbe.order.dto.requests;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.domain.GoodsInfo;
import com.cherrydev.cherrymarketbe.order.entity.ProductDetails;
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
