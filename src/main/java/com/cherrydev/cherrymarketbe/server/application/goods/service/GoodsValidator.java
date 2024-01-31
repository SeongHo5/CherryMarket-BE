package com.cherrydev.cherrymarketbe.server.application.goods.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.DiscontinuedGoodsException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.InsufficientStockException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.OnSaleGoodsException;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInventoryResponseDto;
import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.INSUFFICIENT_STOCK;
import static com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsService.DISCONTINUED_GOODS_STATUS;

@Component
@RequiredArgsConstructor
public class GoodsValidator {

    private final GoodsMapper goodsMapper;

    public void checkInventoryStatus(Long goodsId, int requestedQuantity) {
        GoodsInventoryResponseDto goodsInventory = goodsMapper.findInventoryByGoodsId(goodsId);

        if (goodsInventory == null) {
            // 상품 조회 실패시 예외 발생
            throw new NotFoundException(NOT_FOUND_GOODS);
        } else if (goodsInventory.getSalesStatus().equals(DISCONTINUED_GOODS_STATUS)) {
            throw new DiscontinuedGoodsException(DISCONTINUED_GOODS);
        } else if (goodsInventory.getSalesStatus().equals("PAUSE")) {
            throw new DiscontinuedGoodsException(PAUSE_GOODS);
        } else if (goodsInventory.getInventory() < requestedQuantity) {
            throw new InsufficientStockException(INSUFFICIENT_STOCK);
        }

    }

    protected void checkProductIsNotInSale(Goods goods) {
        if ("ON_SALE".equals(goods.getSalesStatus())) {
            throw new OnSaleGoodsException(ON_SALE_GOODS);
        }
    }

}
