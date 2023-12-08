package com.cherrydev.cherrymarketbe.customer.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.admin.repository.CouponMapper;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerCoupon;
import com.cherrydev.cherrymarketbe.customer.repository.CustomerCouponMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.ALREADY_EXIST_COUPON;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.NOT_FOUND_COUPON;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerCouponMapper customerCouponMapper;
    private final CouponMapper couponMapper;

    public void registerCoupon(
            final AccountDetails accountDetails,
            final String couponCode
    ) {
        Long accountId = accountDetails.getAccount().getAccountId();
        Long couponId = couponMapper.findIdByCode(couponCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUPON));
        checkCouponExistence(couponId, accountId);

        CustomerCoupon customerCoupon = toEntity(accountId, couponId);
        customerCouponMapper.save(customerCoupon);
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
            throw new DuplicatedException(ALREADY_EXIST_COUPON);
        }
    }

}
