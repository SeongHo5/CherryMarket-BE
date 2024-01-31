package com.cherrydev.cherrymarketbe.server.domain.order.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetails {

    @NotNull
    private Long orderId;

    @NotNull
    private Long accountId;

    @NotNull
    private Long goodsId;

    @NotNull
    private Integer quantity;

}
