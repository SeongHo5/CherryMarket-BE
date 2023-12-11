package com.cherrydev.cherrymarketbe.customer.repository;

import com.cherrydev.cherrymarketbe.customer.entity.CustomerCoupon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerCouponMapper {

    void save(CustomerCoupon customerCoupon);

    List<CustomerCoupon> findAllByAccountId(Long accountId);

    boolean existByCouponIdAndAccountId(Long couponId, Long accountId);

}
