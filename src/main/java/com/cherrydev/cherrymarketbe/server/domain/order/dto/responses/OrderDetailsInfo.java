package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import java.util.List;

public record OrderDetailsInfo(

ConsumerInfo consumerInfo,
AmountInfo amountInfo,
ShippingDetailsInfo shippingDetailsInfo,
List<GoodsDetailsInfo> goodsDetailsInfo

) {

    public static OrderDetailsInfo getDetailInfo(ConsumerInfo consumerInfo,
                                                 AmountInfo amountInfo,
                                                 ShippingDetailsInfo shippingDetailsInfo,
                                                 List<GoodsDetailsInfo> goodsDetailsInfo) {

        return new OrderDetailsInfo(
                consumerInfo,
                amountInfo,
                shippingDetailsInfo,
                goodsDetailsInfo
        );
    }


}
