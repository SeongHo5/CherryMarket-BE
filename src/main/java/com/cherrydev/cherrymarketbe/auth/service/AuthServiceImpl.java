package com.cherrydev.cherrymarketbe.auth.service;

import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.enums.UserStatus;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.common.exception.AuthException;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.*;
import com.cherrydev.cherrymarketbe.common.service.RedisService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.account.enums.UserStatus.DELETED;
import static com.cherrydev.cherrymarketbe.account.enums.UserStatus.RESTRICTED;
import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.*;
import static com.cherrydev.cherrymarketbe.common.constant.EmailConstant.*;
import static com.cherrydev.cherrymarketbe.common.constant.EmailConstant.PREFIX_PW_RESET;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.common.utils.CodeGenerator.generateRandomPassword;
import static org.springframework.beans.propertyeditors.CustomBooleanEditor.VALUE_TRUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    private static final int NEW_PASSWORD_LENGTH = 12;

    /**
     * 로그인 처리를 위해 사용자를 조회하고, 비밀번호를 검증한다.
     *
     * @param signInRequestDto 로그인 요청 정보(E-mail, PW)
     * @return 로그인 응답 정보(토큰, 토큰 유효기간, 사용자 이름, 사용자 권한)
     */
    @Override
    @Transactional
    public ResponseEntity<SignInResponseDto> signIn(
            final SignInRequestDto signInRequestDto
    ) {
        String email = signInRequestDto.getEmail();
        String requestedPassword = signInRequestDto.getPassword();
        Account account = findAccountByEmail(email);

        checkUserStatusByEmail(account);
        checkPasswordIsCorrect(requestedPassword, account);

        final JwtResponseDto jwtResponseDto = jwtProvider.createJwtToken(email);
        redisService.setDataExpire(email, jwtResponseDto.getRefreshToken(),
                REFRESH_TOKEN_EXPIRE_TIME);

        return ResponseEntity
                .ok()
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + jwtResponseDto.getAccessToken())
                .body(
                        SignInResponseDto.builder()
                                .userName(account.getName())
                                .userRole(account.getUserRole())
                                .grantType(jwtResponseDto.getGrantType())
                                .accessToken(jwtResponseDto.getAccessToken())
                                .refreshToken(jwtResponseDto.getRefreshToken())
                                .expiresIn(jwtResponseDto.getAccessTokenExpiresIn())
                                .build()
                );
    }

    /**
     * 로그아웃 처리를 위해 사용자 인증 수단(토큰)을 검증하고, 무효화한다.
     *
     * @param jwtRequestDto 인증 수단(토큰)
     */
    @Override
    @Transactional
    public void signOut(final JwtRequestDto jwtRequestDto) {
        jwtProvider.validateToken(jwtRequestDto.getAccessToken());

        Claims claims = jwtProvider.getInfoFromToken(jwtRequestDto.getAccessToken());
        String email = claims.getSubject();

        invalidateToken(email, jwtRequestDto.getAccessToken());
    }

    /**
     * 토큰 재발급
     *
     * @param jwtRequestDto 기존 토큰
     * @return 재발급된 토큰
     */
    @Override
    @Transactional
    public ResponseEntity<JwtReissueResponseDto> reissue(final JwtRequestDto jwtRequestDto) {

        validateRefreshToken(jwtRequestDto);
        validateRefreshTokenOwnership(jwtRequestDto);

        String email = jwtProvider.getInfoFromToken(jwtRequestDto.getAccessToken()).getSubject();

        JwtResponseDto jwtResponseDto = jwtProvider.createJwtToken(email);

        return ResponseEntity
                .ok()
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + jwtResponseDto.getAccessToken())
                .body(
                        JwtReissueResponseDto.builder()
                                .accessToken(jwtResponseDto.getAccessToken())
                                .refreshToken(jwtRequestDto.getRefreshToken())
                                .accessTokenExpiresIn(jwtResponseDto.getAccessTokenExpiresIn())
                                .build()
                );
    }

    /**
     * 비밀번호 재설정 코드 검증 & 비밀번호 재설정
     *
     * @param email            이메일
     * @param verificationCode 인증 코드
     * @return 재설정된 비밀번호
     */
    @Transactional
    public ResponseEntity<String> verifyPasswordResetEmail(
            final String email,
            final String verificationCode
    ) {
        verifyResetCode(email, verificationCode);

        String newPassword = generateRandomPassword(NEW_PASSWORD_LENGTH);
        String encodedPassword = passwordEncoder.encode(newPassword);

        Account account = findAccountByEmail(email);
        account.updatePassword(encodedPassword);
        accountMapper.updateAccountInfo(account);

        return ResponseEntity
                .ok()
                .body(newPassword);
    }

    /**
     * 본인 인증 코드 검증
     * <p>
     * 인증에 성공하면 1일간 인증된 이메일로 등록
     *
     * @param email 인증 코드를 보냈던 이메일
     * @param code  검증할(사용자가 입력한) 인증 코드
     */
    public ResponseEntity<Void> verifyEmail(final String email, final String code) {
        String validCode = redisService.getData(PREFIX_VERIFY + email);
        if (!code.equals(validCode)) {
            throw new AuthException(INVALID_EMAIL_VERIFICATION_CODE);
        }
        redisService.setDataExpire(PREFIX_VERIFIED + email, VALUE_TRUE, WHITE_LIST_VERIFIED_TIME);
        redisService.deleteData(PREFIX_VERIFY + email);
        return ResponseEntity.ok().build();
    }

    // =============== PRIVATE METHODS =============== //

    /**
     * 이메일로 사용자를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 조회된 사용자
     */
    private Account findAccountByEmail(final String email) {
        return accountMapper.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));
    }

    /**
     * 계정의 상태를 검증한다.
     *
     * @param account 계정 정보
     * @throws NotFoundException 계정이 삭제(DELETED) 상태일 경우
     * @throws AuthException     계정이 제한(RESTRICTED) 상태일 경우
     * @see UserStatus
     */
    protected void checkUserStatusByEmail(Account account) {
        if (account.getUserStatus().equals(DELETED)) {
            throw new NotFoundException(DELETED_ACCOUNT);
        }
        if (account.getUserStatus().equals(RESTRICTED)) {
            throw new AuthException(RESTRICTED_ACCOUNT);
        }
    }

    /**
     * 요청된 비밀번호와 계정의 비밀번호가 일치하는지 검증한다.
     */
    private void checkPasswordIsCorrect(String requestedPassword, Account account) {
        if (!passwordEncoder.matches(requestedPassword, account.getPassword())) {
            throw new AuthException(INVALID_ID_OR_PW);
        }
    }

    /**
     * 토큰의 유효성을 검증한다.
     */
    private void validateRefreshToken(JwtRequestDto jwtRequestDto) {
        jwtProvider.validateToken(jwtRequestDto.getRefreshToken());
    }

    /**
     * 요청자와 토큰의 소유자 정보가 일치하는지 검증한다.
     */
    private void validateRefreshTokenOwnership(JwtRequestDto jwtRequestDto) {
        String email = jwtProvider.getInfoFromToken(jwtRequestDto.getAccessToken()).getSubject();
        String validRefreshToken = redisService.getData(email);
        if (!jwtRequestDto.getRefreshToken().equals(validRefreshToken)) {
            throw new AuthException(EXPIRED_REFRESH_TOKEN);
        }
    }

    /**
     * REDIS에서 사용자의 정보를 삭제하고, 토큰을 BLACK_LIST에 추가해 토큰을 무효화한다.
     */
    private void invalidateToken(String email, String accessToken) {
        redisService.deleteData(email);
        redisService.setDataExpire(
                BLACK_LIST_KEY_PREFIX + accessToken,
                VALUE_TRUE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    /**
     * 비밀번호 재설정 코드 검증
     *
     * @return 검증 성공 여부
     */
    private void verifyResetCode(final String email, final String code) {
        String validCode = redisService.getData(PREFIX_PW_RESET + email);
        if (!code.equals(validCode)) {
            throw new AuthException(INVALID_EMAIL_VERIFICATION_CODE);
        }
        redisService.deleteData(PREFIX_PW_RESET + email);
    }
}
