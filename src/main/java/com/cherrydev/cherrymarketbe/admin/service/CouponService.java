package com.cherrydev.cherrymarketbe.admin.service;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.admin.dto.CouponInfoDto;
import com.cherrydev.cherrymarketbe.admin.dto.GrantCouponByAdminDto;
import com.cherrydev.cherrymarketbe.admin.dto.IssueCouponDto;
import com.cherrydev.cherrymarketbe.admin.entity.Coupon;
import com.cherrydev.cherrymarketbe.admin.repository.CouponMapper;
import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerCoupon;
import com.cherrydev.cherrymarketbe.customer.repository.CustomerCouponMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.PAGE_HEADER;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.createPage;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final AccountMapper accountMapper;
    private final CouponMapper couponMapper;
    private final CustomerCouponMapper customerCouponMapper;

    @Transactional
    public void issueCoupon(final IssueCouponDto issueCouponDto) {
        checkCouponIsValid(issueCouponDto.getCode());

        Coupon coupon = issueCouponDto.toEntity();
        couponMapper.save(coupon);
    }

    @Transactional
    public void grantCoupon(final GrantCouponByAdminDto grantCouponByAdminDto) {
        Account account = accountMapper.findByEmail(grantCouponByAdminDto.email())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));
        Long couponId = couponMapper.findIdByCode(grantCouponByAdminDto.couponCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUPON));

        CustomerCoupon customerCoupon = CustomerCoupon.builder()
                .accountId(account.getAccountId())
                .couponId(couponId)
                .build();

        customerCouponMapper.save(customerCoupon);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<MyPage<CouponInfoDto>> getAllCoupons(
            final Pageable pageable
    ) {
        MyPage<CouponInfoDto> infoPage = createPage(pageable, couponMapper::findAll);

        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    // =============== PRIVATE METHODS =============== //

    private void checkCouponIsValid(final String code) {
        if (couponMapper.existByCode(code)) {
            throw new ServiceFailedException(INVALID_INPUT_VALUE);
        }
    }
}
