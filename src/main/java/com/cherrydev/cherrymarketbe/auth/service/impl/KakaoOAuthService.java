package com.cherrydev.cherrymarketbe.auth.service.impl;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.*;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.kakao.KakaoAccountResponse;
import com.cherrydev.cherrymarketbe.auth.service.OAuthService;
import com.cherrydev.cherrymarketbe.common.exception.AuthException;
import com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus;
import com.cherrydev.cherrymarketbe.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

import static com.cherrydev.cherrymarketbe.account.enums.RegisterType.KAKAO;
import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.*;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.FAILED_HTTP_ACTION;
import static com.cherrydev.cherrymarketbe.common.utils.HttpEntityUtils.createHttpEntity;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {

    @Value("${oauth.kakao.clientId}")
    private String kakaoClientId;

    @Value("${oauth.kakao.clientSecret}")
    private String kakaoClientSecret;

    private final CommonOAuthService commonOAuthService;
    private final RestTemplate restTemplate;
    private final RedisService redisService;

    /**
     * 소셜 로그인
     * <p>
     * OAuth 인가 코드를 받아서 토큰을 발급받고, 토큰을 이용해 계정 정보를 가져온다.
     */
    @Override
    @Transactional
    public ResponseEntity<SignInResponseDto> signIn(
            final OAuthRequestDto oAuthRequestDto
    ) {
        String authCode = oAuthRequestDto.getAuthCode();

        OAuthTokenResponseDto tokenResponse = getOAuthToken(authCode);

        String accessToken = commonOAuthService.getAcessTokenIfExist(tokenResponse);
        OAuthAccountInfoDto accountInfo = getAccountInfo(accessToken);

        redisService.saveKakaoTokenToRedis(tokenResponse, accountInfo.getEmail());
        return commonOAuthService.processSignIn(accountInfo, KAKAO.name());
    }

    @Override
    public ResponseEntity<Void> signOut(
            final AccountDetails accountDetails
    ) {
        String email = accountDetails.getUsername();
        String oAuthAccessToken = redisService.getData(OAUTH_KAKAO_PREFIX + email);
        sendLogoutRequest(oAuthAccessToken);

        redisService.deleteKakaoTokenFromRedis(email);

        return ResponseEntity.ok().build();
    }

    // =============== PRIVATE METHODS =============== //

    /**
     * [KAKAO]OAuth 인가 코드를 이용해 토큰을 발급받는다.
     * @param authCode OAuth 인가 코드
     * @return OAuth 토큰
     */
    private OAuthTokenResponseDto getOAuthToken(final String authCode) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_AUTH_URL)
                .queryParam("grant_type", GRANT_TYPE_AUTHORIZATION)
                .queryParam("client_id", kakaoClientId)
                .queryParam("redirect_uri", KAKAO_REDIRECT_URI)
                .queryParam("code", authCode)
                .queryParam("client_secret", kakaoClientSecret);

        HttpEntity<String> entity = createHttpEntity();
        ResponseEntity<OAuthTokenResponseDto> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.POST, entity, OAuthTokenResponseDto.class
        );

        return response.getBody();
    }

    /**
     * [KAKAO]OAuth 인증이 완료된 사용자의 정보를 가져온다.
     * @param accessToken OAuth 인증 토큰
     * @return 사용자 정보
     */
    private OAuthAccountInfoDto getAccountInfo(final String accessToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_USER_INFO_URL);

        HttpEntity<String> entity = createHttpEntity(accessToken);
        ResponseEntity<KakaoAccountResponse> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, entity, KakaoAccountResponse.class
        );

        return OAuthAccountInfoDto.builder()
                .kakaoResponse(Objects.requireNonNull(response.getBody()))
                .kakaoAccount(Objects.requireNonNull(response.getBody()).getKakaoAccount())
                .build();
    }

    /**
     * OAuth 사용자의 로그아웃 요청을 처리한다.
     * @param accessToken OAuth 인증 토큰
     */
    private void sendLogoutRequest(final String accessToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_USER_LOGOUT_URL);

        HttpEntity<String> entity = createHttpEntity(accessToken);

        restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
    }

}
