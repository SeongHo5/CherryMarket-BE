package com.cherrydev.cherrymarketbe.server.infrastructure.repository;

import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.PaymentDetailsInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.PaymentDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {

    PaymentDetailsInfo getByOrderCode(String orderCode);

    PaymentDetailsInfo findByOrderCode(String orderCode);

    void save(PaymentDetails paymentDetails);

    void saveCancelData(PaymentDetails paymentDetails);

}
