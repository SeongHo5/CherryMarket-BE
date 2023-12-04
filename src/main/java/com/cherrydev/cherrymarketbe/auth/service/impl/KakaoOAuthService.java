package com.cherrydev.cherrymarketbe.auth.service.impl;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.account.service.impl.AccountServiceImpl;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.*;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.kakao.KakaoAccountResponse;
import com.cherrydev.cherrymarketbe.auth.service.OAuthService;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
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

import static com.cherrydev.cherrymarketbe.account.enums.RegisterType.LOCAL;
import static com.cherrydev.cherrymarketbe.account.enums.UserRole.ROLE_CUSTOMER;
import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.*;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {

    @Value("${oauth.kakao.clientId}")
    private String kakaoClientId;

    @Value("${oauth.kakao.clientSecret}")
    private String kakaoClientSecret;

    private final AccountMapper accountMapper;
    private final AccountServiceImpl accountService;
    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;
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

        OAuthAccountInfoDto accountInfo = getAccountInfo(tokenResponse.getAccessToken());
        String email = accountInfo.getEmail();
        String userName = accountInfo.getName();

        checkIfAccountLocallyRegistered(accountInfo);

        redisService.saveTokenToRedis(tokenResponse, email);
        JwtResponseDto jwtResponseDto = issueJwtToken(email);

        return createSignInResponse(jwtResponseDto, userName);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> signOut(
            final AccountDetails accountDetails
    ) {
        String email = accountDetails.getUsername();
        String oAuthAccessToken = redisService.getData(OAUTH_KAKAO_PREFIX + email);
        sendLogoutRequest(oAuthAccessToken);

        redisService.deleteKakaoTokenFromRedis(email);

        return ResponseEntity.ok().build();
    }

    /**
     * 사용자가 로컬 계정으로 이미 가입되어 있는지 확인하고, 가입되어 있지 않다면 가입한다.
     */
    public void checkIfAccountLocallyRegistered(final OAuthAccountInfoDto accountInfo) {
        String email = accountInfo.getEmail();
        if (accountMapper.existByEmailAndRegistType(email, LOCAL)) {
            throw new DuplicatedException(LOCAL_ACCOUNT_ALREADY_EXIST);
        }
        if (!accountMapper.existByEmail(email)) {
            accountService.createAccountByOAuth(accountInfo);
        }
    }

    // =============== PRIVATE METHODS =============== //

    /**
     * OAuth 인가 코드를 이용해 토큰을 발급받는다.
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
     * OAuth 인증이 완료된 사용자의 정보를 가져온다.
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
     * OAuth 인증이 완료된 사용자에게 토큰을 발급한다.
     */
    private JwtResponseDto issueJwtToken(final String email) {
        JwtResponseDto jwtResponseDto = jwtProvider.createJwtToken(email);
        redisService.setDataExpire(email, jwtResponseDto.getRefreshToken(),
                REFRESH_TOKEN_EXPIRE_TIME);
        return jwtResponseDto;
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
     * OAuth 사용자의 로그아웃 요청을 처리한다.
     * @param accessToken OAuth 인증 토큰
     * @return 로그아웃 처리된 사용자의 OAuth ID
     */
    private void sendLogoutRequest(final String accessToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_USER_LOGOUT_URL);

        HttpEntity<String> entity = createHttpEntity(accessToken);

        restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
    }

    /**
     * Kakao API의 요청 형식에 맞게 미리 정의된 HttpEntity를 생성한다.
     * @return HttpEntity
     */
    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>("parameters", headers);
    }

    private HttpEntity<String> createHttpEntity(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);
        return new HttpEntity<>("parameters", headers);
    }

}
