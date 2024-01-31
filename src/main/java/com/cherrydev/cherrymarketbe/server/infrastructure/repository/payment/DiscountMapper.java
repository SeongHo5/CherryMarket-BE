package com.cherrydev.cherrymarketbe.server.infrastructure.repository.payment;

import com.cherrydev.cherrymarketbe.server.domain.discount.dto.request.RequestDiscount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiscountMapper {

    List<RequestDiscount> findAll();

    RequestDiscount findById(Long discountId);

    List<RequestDiscount> findByCode(String discountCode);

    void save(RequestDiscount requestDiscount);

    void updateById(RequestDiscount requestDiscount);

    void deleteById(Long discountId);
}
