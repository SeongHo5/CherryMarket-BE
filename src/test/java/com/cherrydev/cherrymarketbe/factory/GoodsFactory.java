package com.cherrydev.cherrymarketbe.factory;

import com.cherrydev.cherrymarketbe.goods.dto.GoodsDto;

import java.sql.Date;

public class GoodsFactory {

    public static GoodsDto createGoodsDto() {
        return GoodsDto.builder()
                       .goodsId(1L)
                       .makerId(1L)
                       .categoryId(1L)
                       .goodsCode("1234567")
                       .goodsName("테스트상품")
                       .description("테스트 상품 설명")
                       .discountId(1L)
                       .price(10000)
                       .retailPrice(3000)
                       .inventory(100)
                       .storageType("ROOM_TEMPERATURE")
                       .capacity("100g")
                       .expDate("별도표기")
                       .allergyInfo("땅콩 함유")
                       .originPlace("국내산")
                       .salesStatus("ON_SALE")
                       .createDate(Date.valueOf("2023-11-29"))
                       .updateDate(Date.valueOf("2023-11-29"))
                       .build();
    }

}
