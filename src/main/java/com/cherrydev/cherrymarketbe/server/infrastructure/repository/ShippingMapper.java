package com.cherrydev.cherrymarketbe.server.infrastructure.repository;

import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerAddress;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.ShippingDetailsInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.ShippingDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShippingMapper {

    CustomerAddress findByDefaultAddress(Long accountId);

    void save(ShippingDetails shippingDetails);

    void updateStatus(ShippingDetails shippingDetails);
    ShippingDetailsInfo findByOrderCode(String orderCode);

}
