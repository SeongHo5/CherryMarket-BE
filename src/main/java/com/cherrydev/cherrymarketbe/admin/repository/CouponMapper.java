package com.cherrydev.cherrymarketbe.admin.repository;

import com.cherrydev.cherrymarketbe.admin.dto.AdminCouponInfoDto;
import com.cherrydev.cherrymarketbe.admin.entity.Coupon;
import com.cherrydev.cherrymarketbe.admin.enums.CouponType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CouponMapper {


    void save(Coupon coupon);
    void update(Coupon coupon);

    void delete(Coupon coupon);

    List<AdminCouponInfoDto> findAll();
    List<Coupon> findAllByType(CouponType type);

    Optional<Long> findIdByCode(String code);
    boolean existByCode(String code);
}
