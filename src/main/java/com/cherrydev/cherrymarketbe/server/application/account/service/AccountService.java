package com.cherrydev.cherrymarketbe.server.application.account.service;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestModifyAccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.request.RequestSignUp;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthAccountInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;


public interface AccountService {

    void createAccount(final RequestSignUp requestSignUp);

    void createAccountByOAuth(final OAuthAccountInfo oAuthAccountInfo, final String provider);

    ResponseEntity<AccountInfo> modifyAccount(final @AuthenticationPrincipal AccountDetails accountDetails, final RequestModifyAccountInfo requestDto);

    void deleteAccount(final @AuthenticationPrincipal AccountDetails accountDetails);

    ResponseEntity<AccountInfo> getAccountInfo(final @AuthenticationPrincipal AccountDetails accountDetails);

    Account findAccountByEmail(final String email);

    void checkDuplicateEmail(String email);
}
