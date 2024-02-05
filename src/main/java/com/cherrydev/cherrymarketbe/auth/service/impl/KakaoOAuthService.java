package com.cherrydev.cherrymarketbe.auth.service.impl;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.*;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.kakao.KakaoAccountResponse;
import com.cherrydev.cherrymarketbe.auth.service.OAuthService;
import com.cherrydev.cherrymarketbe.common.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        System.out.println("authCode = " + authCode);
        System.out.println("provider = " + oAuthRequestDto.getProvider());
        System.out.println("getState = " + oAuthRequestDto.getState());

        System.out.println("토큰얻기 시작");
        OAuthTokenResponseDto tokenResponse = getOAuthToken(authCode);
        System.out.println("tokenResponse = " + tokenResponse);
        System.out.println("토큰얻기 완료");

        String accessToken = commonOAuthService.getAcessTokenIfExist(tokenResponse);
        System.out.println("accessToken = " + accessToken);

        OAuthAccountInfoDto2 accountInfo2 = getAccountInfo2(accessToken);
        System.out.println("accountInfo2 = " + accountInfo2);

//        OAuthAccountInfoDto accountInfo = getAccountInfo(accessToken);
//        System.out.println("accountInfo = " + accountInfo);
//        redisService.saveKakaoTokenToRedis(tokenResponse, accountInfo.getEmail());

        redisService.saveKakaoTokenToRedis(tokenResponse,accountInfo2.getId());
        return commonOAuthService.processSignInForKakao(accountInfo2, KAKAO.name());
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
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_AUTH_URL)
                    .queryParam("grant_type", GRANT_TYPE_AUTHORIZATION)
                    .queryParam("client_id", kakaoClientId)
                    .queryParam("redirect_uri", KAKAO_REDIRECT_URI)
                    .queryParam("code", authCode)
                    .queryParam("client_secret", kakaoClientSecret);

            HttpEntity<String> entity = createHttpEntity();
            System.out.println("토큰 요청 ? entity = " + entity);
            ResponseEntity<OAuthTokenResponseDto> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.POST, entity, OAuthTokenResponseDto.class
            );
            System.out.println("getOAuthToken response = " + response);

            return response.getBody();
        } catch (Exception e) {
            // 오류가 발생했을 때 로그 출력
            log.error("Error in getOAuthToken", e);
            throw e; // 오류를 다시 던져서 호출자에게 전달
        }
    }

    /**
     * [KAKAO]OAuth 인증이 완료된 사용자의 정보를 가져온다.
     *
     * @param accessToken OAuth 인증 토큰
     * @return
     */
    private OAuthAccountInfoDto getAccountInfo(final String accessToken) {
        System.out.println("유저 정보 얻기 시작");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_USER_INFO_URL);
        System.out.println("builder = " + builder);
        System.out.println("빌드 시작");

        HttpEntity<String> entity = createHttpEntity(accessToken);
        System.out.println("entity 생성 완료 = " + entity);
        ResponseEntity<KakaoAccountResponse> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, entity, KakaoAccountResponse.class

        );
        System.out.println("response 생성 완료 = " + response);
//        return responseBody;
        return OAuthAccountInfoDto.builder()
                .kakaoResponse(Objects.requireNonNull(response.getBody()))
                .kakaoAccount(Objects.requireNonNull(response.getBody()).getKakaoAccount())
                .build();
    }
    private OAuthAccountInfoDto2 getAccountInfo2(final String accessToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_USER_INFO_URL);

        // HTTP 요청 보내기
        HttpEntity<String> entity = createHttpEntity(accessToken);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, entity, String.class
        );

        // JSON 문자열을 그대로 반환
        String responseBody = response.getBody();
        System.out.println("Response Body: " + responseBody);

        // Jackson 라이브러리를 사용하여 JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String id = jsonNode.path("id").asText();
            String nickname = jsonNode.path("properties").path("nickname").asText();

            // 반환 형식에 따라 수정
            return OAuthAccountInfoDto2.builder()
                    .id(id)
                    .nickname(nickname)
                    .build();

        } catch (JsonProcessingException e) {
            // JSON 파싱 중 오류가 발생한 경우 처리
            e.printStackTrace();
            // 예외를 처리하거나 적절한 방식으로 응답
            return null;
        }
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
