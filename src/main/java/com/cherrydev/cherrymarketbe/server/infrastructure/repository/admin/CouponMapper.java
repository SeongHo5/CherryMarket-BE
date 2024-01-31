package com.cherrydev.cherrymarketbe.server.infrastructure.repository.admin;

import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.CouponInfo;
import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Coupon;
import com.cherrydev.cherrymarketbe.server.domain.admin.enums.CouponType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CouponMapper {


    void save(Coupon coupon);
    void update(Coupon coupon);

    void delete(Coupon coupon);

    List<CouponInfo> findAll();

    List<CouponInfo> findById(List<Long> couponIds);

    List<Coupon> findAllByType(CouponType type);

    Optional<Long> findIdByCode(String code);
    boolean existByCode(String code);
}
