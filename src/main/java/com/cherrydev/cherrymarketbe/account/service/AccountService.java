package com.cherrydev.cherrymarketbe.account.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.dto.AccountInfoDto;
import com.cherrydev.cherrymarketbe.account.dto.ModifyAccountInfoRequestDto;
import com.cherrydev.cherrymarketbe.account.dto.SignUpRequestDto;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthAccountInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


public interface AccountService {

    void createAccount(final SignUpRequestDto signUpRequestDto);

    void createAccountByOAuth(final OAuthAccountInfoDto oAuthAccountInfoDto, final String provider);

    ResponseEntity<AccountInfoDto> modifyAccount(final @AuthenticationPrincipal AccountDetails accountDetails, final ModifyAccountInfoRequestDto requestDto);

    void deleteAccount(final @AuthenticationPrincipal AccountDetails accountDetails);

    ResponseEntity<AccountInfoDto> getAccountInfo(final @AuthenticationPrincipal AccountDetails accountDetails);

    Account findAccountByEmail(final String email);

}
