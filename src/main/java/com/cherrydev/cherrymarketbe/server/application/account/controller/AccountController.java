package com.cherrydev.cherrymarketbe.server.application.account.controller;

import com.cherrydev.cherrymarketbe.server.application.account.service.impl.AccountServiceImpl;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestModifyAccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountInfo;
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

    private final AccountServiceImpl accountService;

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(final @Valid @RequestBody RequestSignUp requestSignUp) {
        accountService.createAccount(requestSignUp);
    }

    /**
     * 내 정보 조회
     */
    @GetMapping("/my-info")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<AccountInfo> getAccountInfo(
            final @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        return accountService.getAccountInfo(accountDetails);
    }

    /**
     * 내 정보 수정
     */
    @PatchMapping("/my-info/modify")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER')")
    public ResponseEntity<AccountInfo> modifyAccount(
            final @AuthenticationPrincipal AccountDetails accountDetails,
            final @RequestBody RequestModifyAccountInfo requestDto
    ) {
        return accountService.modifyAccount(accountDetails, requestDto);
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
        accountService.deleteAccount(accountDetails);
    }

    /**
     * 이메일 중복체크 확인
     */
    @GetMapping("/check-email")
    @ResponseStatus(HttpStatus.CREATED)
    public void checkDuplicateEmail(final @Valid @RequestParam String email) {
        accountService.checkDuplicateEmail(email);
    }
}
