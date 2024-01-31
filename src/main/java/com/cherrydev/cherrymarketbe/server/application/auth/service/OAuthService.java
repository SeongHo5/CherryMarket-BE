package com.cherrydev.cherrymarketbe.server.application.auth.service;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.SignInResponse;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.OAuthRequestDto;
import org.springframework.http.ResponseEntity;

public interface OAuthService {

    public ResponseEntity<SignInResponse> signIn(final OAuthRequestDto oAuthRequestDto);

    public ResponseEntity<Void> signOut(final AccountDetails accountDetails);
}
