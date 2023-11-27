package com.cherrydev.cherrymarketbe.auth.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NaverOAuthService implements OAuthService {
    @Override
    public ResponseEntity<SignInResponseDto> signIn(OAuthRequestDto oAuthRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> signOut(AccountDetails accountDetails) {
        return null;
    }
}
