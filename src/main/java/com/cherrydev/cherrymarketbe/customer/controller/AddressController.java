package com.cherrydev.cherrymarketbe.customer.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.customer.dto.address.AddAddressRequestDto;
import com.cherrydev.cherrymarketbe.customer.dto.address.AddressInfoDto;
import com.cherrydev.cherrymarketbe.customer.dto.address.ModifyAddressRequestDto;
import com.cherrydev.cherrymarketbe.customer.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/address")
@PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
public class AddressController {

    private final AddressService addressService;

    /**
     * 배송지 추가
     * @param accountDetails 로그인한 계정 정보
     * @param addAddressRequestDto 추가할 배송지 정보
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestBody AddAddressRequestDto addAddressRequestDto
    ) {
        addressService.addAddress(accountDetails, addAddressRequestDto);
    }

    /**
     * 배송지 목록 가져오기
     * @param accountDetails 로그인한 계정 정보
     * @return 배송지 목록
     */
    @GetMapping("/my-list")
    public ResponseEntity<List<AddressInfoDto>> getMyAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return addressService.getAddress(accountDetails);
    }

    /**
     * 배송지 수정
     * @param accountDetails 로그인한 계정 정보
     * @param modifyAddressRequestDto 수정할 배송지 정보
     */
    @PatchMapping("/modify")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AddressInfoDto> modifyAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestBody ModifyAddressRequestDto modifyAddressRequestDto
    ) {
        return addressService.modifyAddress(accountDetails, modifyAddressRequestDto);
    }

    /**
     * 배송지 삭제
     * @param accountDetails 로그인한 계정 정보
     * @param addressId 삭제할 배송지 ID
     */
    @DeleteMapping("/drop")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestParam Long addressId
    ) {
        addressService.deleteAddress(accountDetails, addressId);
    }




}
