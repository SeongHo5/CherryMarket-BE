package com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods;

import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.ProductDetails;

import java.util.List;

public interface ProductMapper {

    void save(ProductDetails productDetails);

    List<GoodsInfo> findByOrderCode(String orderCode);

}
