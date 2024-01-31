package com.cherrydev.cherrymarketbe.server.application.customer.controller;

import com.cherrydev.cherrymarketbe.server.application.customer.service.AddressService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddAddress;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.AddressInfo;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestModifyAddress;
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
     * 회원가입 시 배송지 추가
     * @param accountEmail 로그인한 계정 정보
     * @param requestAddAddress 추가할 배송지 정보
     */
    @PostMapping("/add-on-signup")
    @ResponseStatus(HttpStatus.OK)
    public void addAressOnSignUp(
            final @RequestParam String accountEmail,
            final @RequestBody RequestAddAddress requestAddAddress
    ) {
        addressService.addAressOnSignUp(accountEmail, requestAddAddress);
    }

    /**
     * 배송지 추가
     * @param accountDetails 로그인한 계정 정보
     * @param requestAddAddress 추가할 배송지 정보
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public void addAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestBody RequestAddAddress requestAddAddress
    ) {
        addressService.addAddress(accountDetails, requestAddAddress);
    }

    /**
     * 배송지 목록 가져오기
     * @param accountDetails 로그인한 계정 정보
     * @return 배송지 목록
     */
    @GetMapping("/my-list")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AddressInfo>> getMyAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return addressService.getAddress(accountDetails);
    }

    /**
     * 배송지 수정
     * @param accountDetails 로그인한 계정 정보
     * @param requestModifyAddress 수정할 배송지 정보
     */
    @PatchMapping("/modify")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<AddressInfo> modifyAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestBody RequestModifyAddress requestModifyAddress
    ) {
        return addressService.modifyAddress(accountDetails, requestModifyAddress);
    }

    /**
     * 배송지 삭제
     * @param accountDetails 로그인한 계정 정보
     * @param addressId 삭제할 배송지 ID
     */
    @DeleteMapping("/drop")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public void deleteAddress(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestParam Long addressId
    ) {
        addressService.deleteAddress(accountDetails, addressId);
    }




}
