package com.cherrydev.cherrymarketbe.auth.service.impl;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthAccountInfoDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthRequestDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthTokenResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.naver.NaverAccountResponse;
import com.cherrydev.cherrymarketbe.auth.service.OAuthService;
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

import static com.cherrydev.cherrymarketbe.account.enums.RegisterType.NAVER;
import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.*;
import static com.cherrydev.cherrymarketbe.common.utils.HttpEntityUtils.createHttpEntity;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NaverOAuthService implements OAuthService {

    @Value("${oauth.naver.clientId}")
    private String naverClientId;

    @Value("${oauth.naver.clientSecret}")
    private String naverClientSecret;

    private final CommonOAuthService commonOAuthService;
    private final RestTemplate restTemplate;
    private final RedisService redisService;
    @Override
    @Transactional
    public ResponseEntity<SignInResponseDto> signIn(final OAuthRequestDto oAuthRequestDto) {
        String authCode = oAuthRequestDto.getAuthCode();
        String state = oAuthRequestDto.getState();

        OAuthTokenResponseDto tokenResponse = getOAuthToken(authCode, state);

        String accessToken = commonOAuthService.getAcessTokenIfExist(tokenResponse);
        OAuthAccountInfoDto accountInfo = getAccountInfo(accessToken);

        redisService.saveNaverTokenToRedis(tokenResponse, accountInfo.getEmail());
        return commonOAuthService.processSignIn(accountInfo, NAVER.name());
    }

    @Override
    public ResponseEntity<Void> signOut(AccountDetails accountDetails) {
        return null;
    }

    /**
     * [NAVER]OAuth 인가 코드를 이용해 토큰을 발급받는다.
     * @param authCode OAuth 인가 코드
     * @return OAuth 토큰
     */
    private OAuthTokenResponseDto getOAuthToken(final String authCode, final String state) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NAVER_AUTH_URL)
                .queryParam("grant_type", GRANT_TYPE_AUTHORIZATION)
                .queryParam("client_id", naverClientId)
                .queryParam("client_secret", naverClientSecret)
                .queryParam("state", state)
                .queryParam("code", authCode);
        HttpEntity<String> entity = createHttpEntity();
        ResponseEntity<OAuthTokenResponseDto> response = restTemplate.postForEntity(builder.toUriString(), entity, OAuthTokenResponseDto.class);
        log.info("response: {}", response.getBody());

        return response.getBody();
    }

    /**
     * [NAVER]OAuth 인증이 완료된 사용자의 정보를 가져온다.
     * @param accessToken OAuth 인증 토큰
     * @return 사용자 정보
     */
    private OAuthAccountInfoDto getAccountInfo(final String accessToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NAVER_USER_INFO_URL);

        HttpEntity<String> entity = createHttpEntity(accessToken);
        ResponseEntity<NaverAccountResponse> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, entity, NaverAccountResponse.class
        );

        return new OAuthAccountInfoDto(Objects.requireNonNull(response.getBody()));
    }

}
