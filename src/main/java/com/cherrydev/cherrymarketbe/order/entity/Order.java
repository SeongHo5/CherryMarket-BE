package com.cherrydev.cherrymarketbe.order.entity;


import com.cherrydev.cherrymarketbe.order.domain.AmountInfo;
import com.cherrydev.cherrymarketbe.order.domain.GoodsInfo;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Long orderId;

    private Long accountId;

    private String orderCode;

    private OrderStatus orderStatus;

    private String orderName;

    private Long representativeGoodsId;

    private String representativeGoodsCode;

    private List<GoodsInfo> goodsInfo;

    private AmountInfo amountInfo;

    private Payment payment;

    private Timestamp createdAt;


    public boolean isDeleted() {
        return this.orderStatus == OrderStatus.DELETED;
    }



}
