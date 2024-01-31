package com.cherrydev.cherrymarketbe.server.domain.goods.entity;

import com.cherrydev.cherrymarketbe.server.domain.discount.entity.Discount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Goods {

    private Long id;
    private Long makerId;
    private String makerName;
    private Long categoryId;
    private String code;
    private String name;
    private String description;
    private Long discountId;
    private Integer discountRate;
    private Integer price;
    private Integer specialPrice;
    private Integer inventory;
    private String custodyType;
    private String capacity;
    private String expiredAt;
    private String allergyInformation;
    private String originPlace;
    private String salesStatus;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
