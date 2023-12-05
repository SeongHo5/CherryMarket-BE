package com.cherrydev.cherrymarketbe.customer.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.customer.dto.address.AddAddressRequestDto;
import com.cherrydev.cherrymarketbe.customer.dto.address.AddressInfoDto;
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
public class AddressController {

    private final AddressService addressService;

    /**
     * 배송지 추가
     * @param accountDetails 로그인한 계정 정보
     * @param addAddressRequestDto 추가할 배송지 정보
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AddressInfoDto>> getMyAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return addressService.getAddress(accountDetails);
    }

    /**
     * 카카오 주소록 가져오기
     *
     * @param accountDetails 로그인한 계정 정보
     * @return 가져온 배송지 개수
     */
    @PostMapping("/kakao/add")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public int addAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return addressService.addAddressFromKakao(accountDetails);
    }

}
