package com.cherrydev.cherrymarketbe.customer.repository;

import com.cherrydev.cherrymarketbe.customer.entity.CustomerCoupon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerCouponMapper {

    void save(CustomerCoupon customerCoupon);

    boolean existByCouponIdAndAccountId(Long couponId, Long accountId);

}
