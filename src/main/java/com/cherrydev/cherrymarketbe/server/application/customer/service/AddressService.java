package com.cherrydev.cherrymarketbe.server.application.customer.service;

import com.cherrydev.cherrymarketbe.server.application.account.service.impl.AccountServiceImpl;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddAddress;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.AddressInfo;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestModifyAddress;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerAddress;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.CustomerAddressMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AccountServiceImpl accountService;
    private final CustomerAddressMapper customerAddressMapper;

    private static final int MAX_ADDRESS_COUNT = 3;

    @Transactional
    public void addAressOnSignUp(
            final String accountEmail,
            final RequestAddAddress requestAddAddress

    ) {
        Account findAccount = accountService.findAccountByEmail(accountEmail);
        CustomerAddress customerAddress = requestAddAddress.toEntity(findAccount);
        customerAddressMapper.save(customerAddress);
    }

    @Transactional
    public void addAddress(
            final AccountDetails accountDetails,
            final RequestAddAddress requestAddAddress
    ) {
        Account account = accountDetails.getAccount();
        CustomerAddress customerAddress = requestAddAddress.toEntity(account);

        checkAddressCount(account);
        checkDefaultAddressAlreadyExists(customerAddress);

        customerAddressMapper.save(customerAddress);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<AddressInfo>> getAddress(
            final AccountDetails accountDetails
    ) {
        Account account = accountDetails.getAccount();
        return ResponseEntity.ok()
                .body(customerAddressMapper.findAllByAccountId(account));
    }

    @Transactional
    public void deleteAddress(
            final AccountDetails accountDetails,
            final Long addressId
    ) {
        Long accountId = accountDetails.getAccount().getAccountId();

        CustomerAddress customerAddress = getCustomerAddress(accountId, addressId);

        customerAddressMapper.delete(customerAddress);

    }

    @Transactional
    public ResponseEntity<AddressInfo> modifyAddress(
            final AccountDetails accountDetails,
            final RequestModifyAddress requestModifyAddress
    ) {
        Long accountId = accountDetails.getAccount().getAccountId();
        Long addressId = requestModifyAddress.getAddressId();
        boolean isSetAsDefault = requestModifyAddress.getIsDefault();

        checkIfRequestBodyIsNull(requestModifyAddress);

        CustomerAddress customerAddress = getCustomerAddress(accountId, addressId);
        if (isSetAsDefault) {
            checkDefaultAddressAlreadyExists(customerAddress);
        }

        customerAddressMapper.update(customerAddress);
        return ResponseEntity.ok()
                .body(buildResponseBody(customerAddress));
    }

    // ==================== PRIVATE METHODS ==================== //

    private CustomerAddress getCustomerAddress(final Long accountId, final Long addressId) {
        return customerAddressMapper.findByIdAndAccountId(accountId, addressId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ADDRESS));
    }

    private void checkDefaultAddressAlreadyExists(final CustomerAddress customerAddress) {
        // 넘어온 값중에 isDefault 값이 true 인지 확인한다.
        boolean isExist = customerAddressMapper.existByAccountIdAndIsDefault(customerAddress);
        if (isExist) {
            throw new ServiceFailedException(DEFAULT_ADDRESS_ALREADY_EXISTS);
        }
    }

    private void checkAddressCount(final Account account) {
        int addressCount = customerAddressMapper.countAllByAccountId(account);
        if (addressCount >= MAX_ADDRESS_COUNT) {
            throw new ServiceFailedException(ADDRESS_COUNT_EXCEEDED);
        }
    }

    private void checkIfRequestBodyIsNull(final RequestModifyAddress requestModifyAddress) {
        boolean isAllNull = Stream.of(
                requestModifyAddress.getIsDefault(),
                requestModifyAddress.getName(),
                requestModifyAddress.getZipcode(),
                requestModifyAddress.getAddress(),
                requestModifyAddress.getAddressDetail()
        ).allMatch(Objects::isNull);

        if (isAllNull) {
            throw new ServiceFailedException(INVALID_INPUT_VALUE);
        }

    }

    private AddressInfo buildResponseBody(final CustomerAddress customerAddress) {
        return AddressInfo.builder()
                .addressId(customerAddress.getAddressId())
                .isDefault(customerAddress.getIsDefault())
                .name(customerAddress.getName())
                .zipcode(customerAddress.getZipCode())
                .address(customerAddress.getAddress())
                .addressDetail(customerAddress.getAddressDetail())
                .build();
    }

}
