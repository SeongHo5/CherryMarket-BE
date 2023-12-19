package com.cherrydev.cherrymarketbe.order.repository;

import com.cherrydev.cherrymarketbe.customer.entity.CustomerAddress;
import com.cherrydev.cherrymarketbe.order.domain.ShippingDetailsInfo;
import com.cherrydev.cherrymarketbe.order.entity.ShippingDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShippingMapper {

    CustomerAddress findByDefaultAddress(Long accountId);

    void save(ShippingDetails shippingDetails);

    void updateStatus(ShippingDetails shippingDetails);
    ShippingDetailsInfo findByOrderCode(String orderCode);

}
