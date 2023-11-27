package com.cherrydev.cherrymarketbe.account.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.dto.AccountInfoDto;
import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.SignUpRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtReissueResponseDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


public interface AccountService {

    void createAccount(final SignUpRequestDto signUpRequestDto);

    void modifyAccount(final @AuthenticationPrincipal AccountDetails accountDetails);

    void deleteAccount(final @AuthenticationPrincipal AccountDetails accountDetails);

    ResponseEntity<AccountInfoDto> getAccountInfo(final @AuthenticationPrincipal AccountDetails accountDetails);

}
