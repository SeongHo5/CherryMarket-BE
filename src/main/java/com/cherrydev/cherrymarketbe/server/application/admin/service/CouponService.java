package com.cherrydev.cherrymarketbe.server.application.admin.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.domain.core.dto.MyPage;
import com.cherrydev.cherrymarketbe.server.application.common.utils.PagingUtil;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.GrantCouponByAdmin;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.IssueCoupon;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.CouponInfo;
import com.cherrydev.cherrymarketbe.server.domain.admin.entity.Coupon;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerCoupon;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.account.AccountMapper;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.admin.CouponMapper;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer.CustomerCouponMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final AccountMapper accountMapper;
    private final CouponMapper couponMapper;
    private final CustomerCouponMapper customerCouponMapper;

    @Transactional
    public void issueCoupon(final IssueCoupon issueCoupon) {
        checkCouponIsValid(issueCoupon.getCode());

        Coupon coupon = issueCoupon.toEntity();
        couponMapper.save(coupon);
    }

    @Transactional
    public void grantCoupon(final GrantCouponByAdmin grantCouponByAdmin) {
        Account account = accountMapper.findByEmail(grantCouponByAdmin.email())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));
        Long couponId = couponMapper.findIdByCode(grantCouponByAdmin.couponCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUPON));

        CustomerCoupon customerCoupon = CustomerCoupon.builder()
                .accountId(account.getAccountId())
                .couponId(couponId)
                .build();

        customerCouponMapper.save(customerCoupon);
    }

    @Transactional(readOnly = true)
    public MyPage<CouponInfo> getAllCoupons(
            final Pageable pageable
    ) {
        return PagingUtil.createPage(pageable, couponMapper::findAll);
    }

    // =============== PRIVATE METHODS =============== //

    private void checkCouponIsValid(final String code) {
        if (couponMapper.existByCode(code)) {
            throw new ServiceFailedException(INVALID_INPUT_VALUE);
        }
    }
}
