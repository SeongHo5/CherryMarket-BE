package com.cherrydev.cherrymarketbe.account.controller;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.dto.AccountInfoDto;
import com.cherrydev.cherrymarketbe.account.service.AccountServiceImpl;
import com.cherrydev.cherrymarketbe.auth.dto.SignUpRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountServiceImpl accountServiceImpl;
//    private final CreditService creditService;

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(final @Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        accountServiceImpl.createAccount(signUpRequestDto);
    }

    /**
     * 내 정보 조회
     */
    @GetMapping("/my-info")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<AccountInfoDto> getAccountInfo(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return accountServiceImpl.getAccountInfo(accountDetails);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/drop-out")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER')")
    public void dropOut(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        accountServiceImpl.deleteAccount(accountDetails);
    }

//    /**
//     * 적립금(크레딧) 추가
//     */
//    @PostMapping("/add-credit")
//    @ResponseStatus(HttpStatus.OK)
//    public void addCredit(
//            final @RequestBody CreditRequestDto creditRequestDto
//    ) {
//        creditService.addCredit(creditRequestDto);
//    }
//
//    /**
//     * 내 적립금 조회
//     * @param accountDetails 로그인한 사용자 정보
//     * @return 적립금 정보(총/사용완료/사용가능/만료)
//     */
//    @GetMapping("/get-credit")
//    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<CustomerCreditSummaryDto> getCredit(
//            final @AuthenticationPrincipal AccountDetails accountDetails
//    ) {
//        return creditService.getCreditAmounts(accountDetails);
//    }

}
