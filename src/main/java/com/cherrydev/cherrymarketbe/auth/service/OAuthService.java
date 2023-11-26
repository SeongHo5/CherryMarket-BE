package com.cherrydev.cherrymarketbe.auth.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthRequestDto;
import org.springframework.http.ResponseEntity;

public interface OAuthService {

    public ResponseEntity<SignInResponseDto> signIn(final OAuthRequestDto oAuthRequestDto);

    public ResponseEntity<?> signOut(final AccountDetails accountDetails);
}
