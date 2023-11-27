package com.cherrydev.cherrymarketbe.auth.service;

import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtReissueResponseDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<SignInResponseDto> signIn(final SignInRequestDto signInRequestDto);

    void signOut(final JwtRequestDto jwtRequestDto);

    ResponseEntity<JwtReissueResponseDto> reissue(
            final JwtRequestDto jwtRequestDto
    );

}
