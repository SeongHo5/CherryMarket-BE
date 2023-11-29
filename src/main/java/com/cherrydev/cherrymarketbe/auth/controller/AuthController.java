package com.cherrydev.cherrymarketbe.auth.controller;

import com.cherrydev.cherrymarketbe.auth.dto.*;
import com.cherrydev.cherrymarketbe.auth.service.AuthServiceImpl;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtReissueResponseDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.common.service.EmailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final EmailService emailService;

    /**
     * 로그인
     */
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(
            final @Valid @RequestBody SignInRequestDto signInRequestDto
    ) {
        return authService.signIn(signInRequestDto);
    }

    /**
     * 로그아웃
     */
    @DeleteMapping("/sign-out")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public void signOut(
            final @RequestBody JwtRequestDto jwtRequestDto
    ) {
        authService.signOut(jwtRequestDto);
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/re-issue")
    public ResponseEntity<JwtReissueResponseDto> reissue(
            final @RequestBody JwtRequestDto jwtRequestDto
            ) {
        return authService.reissue(jwtRequestDto);
    }

    /**
     * 본인 인증 이메일 발송
     *
     * @param email 메일 보낼 주소
     */
    @PostMapping("/send-email")
    @ResponseStatus(HttpStatus.OK)
    public void sendEmail(
            final @Email @RequestParam String email
    ) {
        emailService.sendVerificationMail(email);
    }

    /**
     * 본인 인증 확인
     *
     * @param verificationCode 인증 코드
     */
    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(
            final @RequestParam String email,
            final @RequestParam String verificationCode
    ) {
        return authService.verifyEmail(email, verificationCode);
    }

    /**
     * 비밀번호 재설정 이메일 발송
     * @param email 메일 보낼 주소
     */
    @PostMapping("/send-reset-email")
    @ResponseStatus(HttpStatus.OK)
    public void sendPasswordResetEmail(
            final @Email @RequestParam String email
    ) {
        emailService.sendPasswordResetMail(email);
    }

    /**
     * 비밀번호 재설정 확인
     * @param verificationCode 인증 코드
     */
    @GetMapping("/verify-reset-email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> verifyPasswordResetEmail(
            final @RequestParam String email,
            final @RequestParam String verificationCode
    ) {
        return authService.verifyPasswordResetEmail(email, verificationCode);
    }

}
