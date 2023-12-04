package com.cherrydev.cherrymarketbe.auth.service.impl;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.auth.dto.SignInResponseDto;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.*;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NaverOAuthService {

    @Value("${oauth.naver.clientId}")
    private String naverClientId;

    @Value("${oauth.naver.clientSecret}")
    private String naverClientSecret;

    private final RestTemplate restTemplate;


    public ResponseEntity<SignInResponseDto> signIn(final OAuthRequestDto oAuthRequestDto) {
        String authCode = oAuthRequestDto.getAuthCode();
        String state = oAuthRequestDto.getState();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NAVER_AUTH_URL)
                .queryParam("grant_type", GRANT_TYPE_AUTHORIZATION)
                .queryParam("client_id", naverClientId)
                .queryParam("client_secret", naverClientSecret)
                .queryParam("state", state)
                .queryParam("code", authCode);
        HttpEntity<String> entity = createHttpEntity();
        ResponseEntity<String> response = restTemplate.postForEntity(builder.toUriString(), entity, String.class);
        log.info("response: {}", response.getBody());
        return null;

    }

    public ResponseEntity<Void> signOut(AccountDetails accountDetails) {
        return null;
    }

    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>("parameters", headers);
    }
}
