package com.cherrydev.cherrymarketbe.account.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.dto.AccountInfoDto;
import com.cherrydev.cherrymarketbe.account.dto.ModifyAccountInfoRequestDto;
import com.cherrydev.cherrymarketbe.account.dto.SignUpRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


public interface AccountService {

    void createAccount(final SignUpRequestDto signUpRequestDto);

    ResponseEntity<AccountInfoDto> modifyAccount(final @AuthenticationPrincipal AccountDetails accountDetails, final ModifyAccountInfoRequestDto requestDto);

    void deleteAccount(final @AuthenticationPrincipal AccountDetails accountDetails);

    ResponseEntity<AccountInfoDto> getAccountInfo(final @AuthenticationPrincipal AccountDetails accountDetails);

}
