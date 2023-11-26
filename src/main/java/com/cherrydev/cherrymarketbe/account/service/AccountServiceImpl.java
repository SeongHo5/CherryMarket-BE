package com.cherrydev.cherrymarketbe.account.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.enums.ForbiddenUserName;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthAccountInfoDto;
import com.cherrydev.cherrymarketbe.common.exception.AuthException;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtReissueResponseDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.common.service.RedisService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cherrydev.cherrymarketbe.account.dto.AccountInfoDto;
import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.SignUpRequestDto;
import com.cherrydev.cherrymarketbe.account.entity.Account;

import java.util.Arrays;

import static com.cherrydev.cherrymarketbe.account.enums.RegisterType.*;
import static com.cherrydev.cherrymarketbe.account.enums.UserRole.ROLE_CUSTOMER;
import static com.cherrydev.cherrymarketbe.account.enums.UserStatus.*;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.common.jwt.JwtAuthFilter.BLACK_LIST_KEY_PREFIX;
import static com.cherrydev.cherrymarketbe.common.jwt.JwtProvider.*;
import static com.cherrydev.cherrymarketbe.common.utils.CodeGenerator.generateRandomCode;
import static org.springframework.beans.propertyeditors.CustomBooleanEditor.VALUE_TRUE;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @Override
    @Transactional
    public void createAccount(final SignUpRequestDto signUpRequestDto) {
        String requestedEmail = signUpRequestDto.getEmail();
        String requestedUsername = signUpRequestDto.getName();

        checkUsernameIsProhibited(requestedUsername);
        checkEmailIsDuplicated(requestedEmail);

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
        Account account = signUpRequestDto.toEntity(encodedPassword);

        accountRepository.save(account);
    }

    @Transactional
    public void createAccountByOAuth(final OAuthAccountInfoDto oAuthAccountInfoDto) {
        String email = oAuthAccountInfoDto.getEmail();
        String name = oAuthAccountInfoDto.getName();
        String encodedPassword = passwordEncoder.encode(generateRandomCode(10));

        checkUsernameIsProhibited(name);
        checkEmailIsDuplicated(email);

        Account account = Account.builder()
                .oauthId(oAuthAccountInfoDto.getId())
                .email(email)
                .name(name)
                .password(encodedPassword)
                .contact(oAuthAccountInfoDto.getContact())
                .userStatus(ACTIVE)
                .userRole(ROLE_CUSTOMER)
                .registerType(KAKAO)
                .build();

        accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<AccountInfoDto> getAccountInfo(final AccountDetails accountDetails) {
        Account requestedAccount = accountDetails.getAccount();
        return ResponseEntity
                .ok()
                .body(
                        new AccountInfoDto(requestedAccount)
                );
    }

    @Override
    @Transactional
    public void modifyAccount(final AccountDetails accountDetails) {
        // TODO 곧
    }

    @Override
    @Transactional
    public void deleteAccount(final AccountDetails accountDetails) {
        accountRepository.delete(accountDetails.getAccount());
    }

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
     */
    @Override
    @Transactional
    public void signOut(final JwtRequestDto jwtRequestDto) {
        jwtProvider.validateToken(jwtRequestDto.getAccessToken());


        Claims claims = jwtProvider.getInfoFromToken(jwtRequestDto.getAccessToken());
        String email = claims.getSubject();

        invalidateToken(email, jwtRequestDto.getAccessToken());
    }

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

    // =============== PRIVATE METHODS =============== //

    private void checkPasswordIsCorrect(String requestedPassword, Account account) {
        if (!passwordEncoder.matches(requestedPassword, account.getPassword())) {
            throw new AuthException(INVALID_ID_OR_PW);
        }
    }

    private void checkUserStatusByEmail(Account account) {
        if (account.getUserStatus().equals(DELETED)) {
            throw new NotFoundException(DELETED_ACCOUNT);
        }
        if (account.getUserStatus().equals(RESTRICTED)) {
            throw new AuthException(RESTRICTED_ACCOUNT);
        }
    }

    private Account findAccountByEmail(final String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));
    }

    private void checkEmailIsDuplicated(String email) {
        if (accountRepository.existByEmail(email)) {
            throw new DuplicatedException(CONFLICT_ACCOUNT);
        }
    }

    private void checkUsernameIsProhibited(String username) {
        // 금지 이름을 contain하는지 검사
        boolean isProhibited = Arrays.stream(ForbiddenUserName.values())
                .anyMatch(forbiddenUserName -> username.contains(forbiddenUserName.getName()));

        if (isProhibited) {
            throw new AuthException(PROHIBITED_USERNAME);
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
}
