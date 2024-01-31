package com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer;

import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerCoupon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerCouponMapper {

    void save(CustomerCoupon customerCoupon);

    List<CustomerCoupon> findAllByAccountId(Long accountId);

    boolean existByCouponIdAndAccountId(Long couponId, Long accountId);

}
