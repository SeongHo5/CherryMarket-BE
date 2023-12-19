package com.cherrydev.cherrymarketbe.order.repository;

import com.cherrydev.cherrymarketbe.order.domain.GoodsInfo;
import com.cherrydev.cherrymarketbe.order.entity.ProductDetails;

import java.util.List;

public interface ProductMapper {

    void save(ProductDetails productDetails);

    List<GoodsInfo> findByOrderCode(String orderCode);

}
