package com.cherrydev.cherrymarketbe.server.infrastructure.repository;

import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.GoodsInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.ProductDetails;

import java.util.List;

public interface ProductMapper {

    void save(ProductDetails productDetails);

    List<GoodsInfo> findByOrderCode(String orderCode);

}
