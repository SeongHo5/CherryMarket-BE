package com.cherrydev.cherrymarketbe.auth.service.impl;

import com.cherrydev.cherrymarketbe.account.enums.RegisterType;
import com.cherrydev.cherrymarketbe.account.service.impl.AccountServiceImpl;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthAccountInfoDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthAccountInfoDto2;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthTokenResponseDto;
import com.cherrydev.cherrymarketbe.common.exception.AuthException;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.cherrydev.cherrymarketbe.account.enums.RegisterType.LOCAL;
import static com.cherrydev.cherrymarketbe.account.enums.UserRole.ROLE_CUSTOMER;
import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.REFRESH_TOKEN_EXPIRE_TIME;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;

@Service
@RequiredArgsConstructor
public class CommonOAuthService {

    private final AccountServiceImpl accountService;
    private final AccountMapper accountMapper;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public ResponseEntity<SignInResponseDto> processSignInForKakao(final OAuthAccountInfoDto2 accountInfo, final String provider) {
//        String email = accountInfo.getEmail();
        String email = accountInfo.getId();
        String userName = accountInfo.getName();

        checkAndProcessOAuthRegistration(accountInfo, provider);
        JwtResponseDto jwtResponseDto = issueJwtToken(email);

        return createSignInResponse(jwtResponseDto, userName);
    }

    public ResponseEntity<SignInResponseDto> processSignIn(final OAuthAccountInfoDto accountInfo, final String provider) {
//        String email = accountInfo.getEmail();
        String email = accountInfo.getEmail();
        String userName = accountInfo.getName();

        checkAndProcessOAuthRegistration(accountInfo, provider);
        JwtResponseDto jwtResponseDto = issueJwtToken(email);

        return createSignInResponse(jwtResponseDto, userName);
    }

    /**
     * 소셜 로그인 응답을 생성한다.
     * <p>
     * 소셜 로그인은 고객만 가능하므로, 응답 DTO의 userRole은 ROLE_CUSTOMER로 고정
     */
    private ResponseEntity<SignInResponseDto> createSignInResponse(
            final JwtResponseDto jwtResponseDto,
            final String userName
    ) {
        return ResponseEntity.ok()
                .body(
                        SignInResponseDto.builder()
                                .userName(userName)
                                .userRole(ROLE_CUSTOMER)
                                .grantType(jwtResponseDto.getGrantType())
                                .accessToken(jwtResponseDto.getAccessToken())
                                .refreshToken(jwtResponseDto.getRefreshToken())
                                .expiresIn(jwtResponseDto.getAccessTokenExpiresIn())
                                .build()
                );
    }

    /**
     * 사용자가 로컬 계정으로 이미 가입되어 있는지 확인하고, 가입되어 있지 않다면 가입한다.
     */
    private void checkAndProcessOAuthRegistration(final OAuthAccountInfoDto accountInfo, final String provider) {
        String email = accountInfo.getEmail();
        RegisterType type = accountMapper.getRegisterTypeByEmail(email);

        checkOAuthAccountType(type, provider);

        if (!accountMapper.existByEmail(email)) {
            accountService.createAccountByOAuth(accountInfo, provider);
        }
    }

    private void checkAndProcessOAuthRegistration(final OAuthAccountInfoDto2 accountInfo, final String provider) {
        String email = accountInfo.getId();
        RegisterType type = accountMapper.getRegisterTypeByEmail(email);

        checkOAuthAccountType(type, provider);

        if (!accountMapper.existByEmail(email)) {
            accountService.createAccountByOAuth(accountInfo, provider);
        }
    }

    /**
     * OAuth 인증이 완료된 사용자에게 토큰을 발급한다.
     */
    private JwtResponseDto issueJwtToken(final String email) {
        JwtResponseDto jwtResponseDto = jwtProvider.createJwtToken(email);
        redisService.setDataExpire(email, jwtResponseDto.getRefreshToken(),
                REFRESH_TOKEN_EXPIRE_TIME);
        return jwtResponseDto;
    }

    /**
     * OAuth 응답에 토큰이 정상 반환되었는지 확인하고, 있다면 토큰을 반환한다.
     * @param tokenResponse OAuth Response
     * @return AcessToken
     */
    public String getAcessTokenIfExist(final OAuthTokenResponseDto tokenResponse) {
        String accessToken = tokenResponse.getAccessToken();
        if (accessToken == null) {
            throw new AuthException(FAILED_HTTP_ACTION);
        }
        return accessToken;
    }

    private void checkOAuthAccountType(RegisterType type, String provider) {
        if (type != null && type.equals(LOCAL)){
            throw new DuplicatedException(LOCAL_ACCOUNT_ALREADY_EXIST);
        }
        if (type != null && !type.equals(RegisterType.valueOf(provider))) {
            throw new ServiceFailedException(INVALID_OAUTH_TYPE);
        }
    }
}
