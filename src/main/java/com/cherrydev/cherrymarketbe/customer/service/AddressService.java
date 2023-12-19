package com.cherrydev.cherrymarketbe.customer.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.service.impl.AccountServiceImpl;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.customer.dto.address.AddAddressRequestDto;
import com.cherrydev.cherrymarketbe.customer.dto.address.AddressInfoDto;
import com.cherrydev.cherrymarketbe.customer.dto.address.ModifyAddressRequestDto;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerAddress;
import com.cherrydev.cherrymarketbe.customer.repository.CustomerAddressMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;

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
            final AddAddressRequestDto addAddressRequestDto

    ) {
        Account findAccount = accountService.findAccountByEmail(accountEmail);
        CustomerAddress customerAddress = addAddressRequestDto.toEntity(findAccount);
        customerAddressMapper.save(customerAddress);
    }

    @Transactional
    public void addAddress(
            final AccountDetails accountDetails,
            final AddAddressRequestDto addAddressRequestDto
    ) {
        Account account = accountDetails.getAccount();
        CustomerAddress customerAddress = addAddressRequestDto.toEntity(account);

        checkAddressCount(account);
        checkDefaultAddressAlreadyExists(customerAddress);

        customerAddressMapper.save(customerAddress);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<AddressInfoDto>> getAddress(
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
    public ResponseEntity<AddressInfoDto> modifyAddress(
            final AccountDetails accountDetails,
            final ModifyAddressRequestDto modifyAddressRequestDto
    ) {
        Long accountId = accountDetails.getAccount().getAccountId();
        Long addressId = modifyAddressRequestDto.getAddressId();
        boolean isSetAsDefault = modifyAddressRequestDto.getIsDefault();

        checkIfRequestBodyIsNull(modifyAddressRequestDto);

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

    private void checkIfRequestBodyIsNull(final ModifyAddressRequestDto modifyAddressRequestDto) {
        boolean isAllNull = Stream.of(
                modifyAddressRequestDto.getIsDefault(),
                modifyAddressRequestDto.getName(),
                modifyAddressRequestDto.getZipcode(),
                modifyAddressRequestDto.getAddress(),
                modifyAddressRequestDto.getAddressDetail()
        ).allMatch(Objects::isNull);

        if (isAllNull) {
            throw new ServiceFailedException(INVALID_INPUT_VALUE);
        }

    }

    private AddressInfoDto buildResponseBody(final CustomerAddress customerAddress) {
        return AddressInfoDto.builder()
                .addressId(customerAddress.getAddressId())
                .isDefault(customerAddress.getIsDefault())
                .name(customerAddress.getName())
                .zipcode(customerAddress.getZipCode())
                .address(customerAddress.getAddress())
                .addressDetail(customerAddress.getAddressDetail())
                .build();
    }


}
