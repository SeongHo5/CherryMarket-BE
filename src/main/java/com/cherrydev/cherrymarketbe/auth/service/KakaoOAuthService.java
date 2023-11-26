package com.cherrydev.cherrymarketbe.auth.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.account.service.AccountServiceImpl;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.*;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.kakao.KakaoAccountResponse;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
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
import static com.cherrydev.cherrymarketbe.auth.constants.OAuthConstant.*;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.common.jwt.JwtProvider.*;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {

    @Value("${oauth.kakao.clientId}")
    private String kakaoClientId;

    @Value("${oauth.kakao.clientSecret}")
    private String kakaoClientSecret;

    private final AccountMapper accountRepository;
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

        saveTokenToRedis(tokenResponse, email);
        JwtResponseDto jwtResponseDto = issueJwtToken(email);

        return createSignInResponse(jwtResponseDto, userName);
    }

    @Override
    @Transactional
    public ResponseEntity<?> signOut(
            final AccountDetails accountDetails
    ) {
        String email = accountDetails.getUsername();
        String oAuthAccessToken = redisService.getData(OAUTH_KAKAO_PREFIX + email);
        String response = sendLogoutRequest(oAuthAccessToken);

//        checkOAuthLogoutResponse(response, email);
        deleteTokenFromRedis(email);

        return ResponseEntity.ok().build();
    }

    /**
     * 사용자가 로컬 계정으로 이미 가입되어 있는지 확인한다.
     */
    @Transactional
    public void checkIfAccountLocallyRegistered(final OAuthAccountInfoDto accountInfo) {
        String email = accountInfo.getEmail();
        if (accountRepository.existByEmailAndRegistType(email, LOCAL)) {
            throw new DuplicatedException(LOCAL_ACCOUNT_ALREADY_EXIST);
        }
        if (!accountRepository.existByEmail(email)) {
            accountService.createAccountByOAuth(accountInfo);
        }
    }

    // =============== PRIVATE METHODS =============== //

    public OAuthAccountInfoDto getAccountInfo(final String accessToken) {
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

    public OAuthTokenResponseDto getOAuthToken(final String authCode) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_AUTH_URL)
                .queryParam("grant_type", OAUTH_KAKAO_GRANT_TYPE)
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

    public String sendLogoutRequest(final String accessToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_USER_LOGOUT_URL);

        HttpEntity<String> entity = createHttpEntity(accessToken);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.POST, entity, String.class
        );
        log.info("Kakao Logout Response: {}", response.getBody());
        return response.getBody();
    }

    public void checkOAuthLogoutResponse(
            final int response,
            final String email
    ) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT));

        long oAuthId = account.getOauthId();

        if (response != oAuthId) {
            throw new ServiceFailedException(FAILED_HTTP_ACTION);
        }
    }

    /**
     * OAuth 인증 토큰을 Redis에 저장한다.
     *
     * @param oAuthTokenResponseDto OAuth 인증 토큰
     * @param email                 사용자 이메일
     */
    private void saveTokenToRedis(
            final OAuthTokenResponseDto oAuthTokenResponseDto,
            final String email
    ) {
        String accessToken = oAuthTokenResponseDto.getAccessToken();
        Long expiresIn = oAuthTokenResponseDto.getExpiresIn();

        String refreshToken = oAuthTokenResponseDto.getRefreshToken();
        Long refreshTokenExpiresIn = oAuthTokenResponseDto.getRefreshTokenExpiresIn();

        redisService.setDataExpire(OAUTH_KAKAO_PREFIX + email, accessToken, expiresIn);
        redisService.setDataExpire(OAUTH_KAKAO_REFRESH_PREFIX + email, refreshToken, refreshTokenExpiresIn);
    }

    /**
     * Redis에 저장된 OAuth 인증 토큰을 삭제한다.
     *
     * @param email 사용자 이메일
     */
    private void deleteTokenFromRedis(final String email) {
        redisService.deleteData(OAUTH_KAKAO_PREFIX + email);
        redisService.deleteData(OAUTH_KAKAO_REFRESH_PREFIX + email);
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

    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>("parameters", headers);
    }

    private HttpEntity<String> createHttpEntity(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);
        return new HttpEntity<>("parameters", headers);
    }

}
