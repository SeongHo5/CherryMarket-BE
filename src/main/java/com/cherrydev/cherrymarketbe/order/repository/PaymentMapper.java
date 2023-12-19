package com.cherrydev.cherrymarketbe.order.repository;

import com.cherrydev.cherrymarketbe.order.domain.PaymentDetailsInfo;
import com.cherrydev.cherrymarketbe.order.entity.PaymentDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {

    PaymentDetailsInfo getByOrderCode(String orderCode);

    PaymentDetailsInfo findByOrderCode(String orderCode);

    void save(PaymentDetails paymentDetails);

    void saveCancelData(PaymentDetails paymentDetails);

}
