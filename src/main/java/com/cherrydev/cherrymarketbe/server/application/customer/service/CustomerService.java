package com.cherrydev.cherrymarketbe.server.application.customer.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.response.CouponInfo;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.CouponMapper;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerCoupon;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.CustomerCouponMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerCouponMapper customerCouponMapper;
    private final CouponMapper couponMapper;

    @Transactional
    public void registerCoupon(
            final AccountDetails accountDetails,
            final String couponCode
    ) {
        Long accountId = accountDetails.getAccount().getAccountId();
        Long couponId = couponMapper.findIdByCode(couponCode)
                .orElseThrow(() -> new NotFoundException(ExceptionStatus.NOT_FOUND_COUPON));
        checkCouponExistence(couponId, accountId);

        CustomerCoupon customerCoupon = toEntity(accountId, couponId);
        customerCouponMapper.save(customerCoupon);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<CouponInfo>> getCouponList(
            final AccountDetails accountDetails
    ) {
        Long accountId = accountDetails.getAccount().getAccountId();

        List<CustomerCoupon> customerCoupons = customerCouponMapper.findAllByAccountId(accountId);
        List<Long> couponIds = customerCoupons.stream()
                .map(CustomerCoupon::getCouponId)
                .toList();
        List<CouponInfo> couponInfos = couponMapper.findById(couponIds);
        return ResponseEntity.ok()
                .body(couponInfos);
    }

    // ============ PRIVATE METHODS ============ //

    private CustomerCoupon toEntity(Long accountId, Long couponId) {
        return CustomerCoupon.builder()
                .accountId(accountId)
                .couponId(couponId)
                .build();
    }

    private void checkCouponExistence(Long couponId, Long accountId) {
        if (customerCouponMapper.existByCouponIdAndAccountId(couponId, accountId)) {
            throw new DuplicatedException(ExceptionStatus.ALREADY_EXIST_COUPON);
        }
    }


}
