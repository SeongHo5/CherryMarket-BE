package com.cherrydev.cherrymarketbe.server.application.account.controller;

import com.cherrydev.cherrymarketbe.server.application.account.service.AccountService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestModifyAccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid final RequestSignUp requestSignUp) {
        accountService.createAccount(requestSignUp);
        return ResponseEntity.ok().build();
    }

    /**
     * 내 정보 조회
     */
    @GetMapping("/my-info")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<AccountInfo> getAccountInfo(
            @AuthenticationPrincipal final AccountDetails accountDetails
    ) {
        AccountInfo accountInfo = accountService.getAccountInfo(accountDetails);
        return ResponseEntity.ok(accountInfo);
    }

    /**
     * 내 정보 수정
     */
    @PatchMapping("/my-info/modify")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER')")
    public ResponseEntity<AccountInfo> modifyAccount(
            @AuthenticationPrincipal final AccountDetails accountDetails,
            @RequestBody final RequestModifyAccountInfo requestDto
    ) {
        AccountInfo accountInfo = accountService.modifyAccount(accountDetails, requestDto);
        return ResponseEntity.ok(accountInfo);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/drop-out")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER')")
    public ResponseEntity<Void> dropOut(
            @AuthenticationPrincipal final AccountDetails accountDetails
    ) {
        accountService.deleteAccount(accountDetails);
        return ResponseEntity.ok().build();
    }

    /**
     * 이메일 중복체크 확인
     */
    @GetMapping("/check-email")
    public ResponseEntity<Void> checkDuplicateEmail(@RequestParam @Email final String email) {
        accountService.existByEmail(email);
        return ResponseEntity.ok().build();
    }
}
