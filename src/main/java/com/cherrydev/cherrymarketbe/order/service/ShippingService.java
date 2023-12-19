package com.cherrydev.cherrymarketbe.order.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerAddress;
import com.cherrydev.cherrymarketbe.order.domain.ShippingDetailsInfo;
import com.cherrydev.cherrymarketbe.order.dto.requests.ShippingStatusRequest;
import com.cherrydev.cherrymarketbe.order.dto.requests.ShippingDetailRequest;
import com.cherrydev.cherrymarketbe.order.entity.ShippingDetails;
import com.cherrydev.cherrymarketbe.order.repository.ShippingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.NOT_FOUND_ADDRESS;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShippingService {

    private final ShippingMapper shippingMapper;

    @Transactional
    public void createDeliveryDetail(AccountDetails accountDetails, Long orderId, String orderCode) {
        ShippingDetails shippingDetails = new ShippingDetailRequest()
                .create(
                        accountDetails,
                        orderId,
                        orderCode,
                        getDefaultAddress(accountDetails)
                );
        shippingMapper.save(shippingDetails);
    }

    @Transactional
    public void updateDeliveryDetail(String orderCode, String deliveryStatus) {
        ShippingStatusRequest requestDto = ShippingStatusRequest.builder()
                .orderCode(orderCode)
                .deliveryStatus(deliveryStatus)
                .build();
        updateDeliveryDetail(requestDto);
    }

    @Transactional
    public void updateDeliveryDetail(ShippingStatusRequest requestDto) {
        ShippingDetails shippingDetails = requestDto.changeDeliveryStatus();
        shippingMapper.updateStatus(shippingDetails);
    }

    @Transactional
    public ShippingDetailsInfo findByOrderCode(String orderCode) {
        return shippingMapper.findByOrderCode(orderCode);
    }

    private CustomerAddress getDefaultAddress(AccountDetails accountDetails) {
        CustomerAddress address = shippingMapper.findByDefaultAddress(accountDetails.getAccount().getAccountId());

        if (address.getAddressId() == null) {
            throw new NotFoundException(NOT_FOUND_ADDRESS);
        }


        return address;
    }


}
