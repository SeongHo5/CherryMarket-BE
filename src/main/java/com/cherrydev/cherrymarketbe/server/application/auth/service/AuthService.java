package com.cherrydev.cherrymarketbe.server.application.auth.service;

import com.cherrydev.cherrymarketbe.server.application.common.jwt.dto.JwtReissueResponseDto;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.RequestSignIn;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.SignInResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<SignInResponse> signIn(final RequestSignIn requestSignIn);

    void signOut(final JwtRequestDto jwtRequestDto);

    ResponseEntity<JwtReissueResponseDto> reissue(final JwtRequestDto jwtRequestDto);

    ResponseEntity<Void> verifyEmail(final String email, final String verificationCode);

    ResponseEntity<String> verifyPasswordResetEmail(final String email, final String verificationCode);

}
