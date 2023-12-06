package com.cherrydev.cherrymarketbe.customer.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.customer.dto.address.AddAddressRequestDto;
import com.cherrydev.cherrymarketbe.customer.dto.address.AddressInfoDto;
import com.cherrydev.cherrymarketbe.customer.entity.CustomerAddress;
import com.cherrydev.cherrymarketbe.account.repository.AccountMapper;
import com.cherrydev.cherrymarketbe.customer.repository.AddressMapper;
import com.cherrydev.cherrymarketbe.auth.dto.oauth.kakao.KakaoAddressInfoDto;
import com.cherrydev.cherrymarketbe.common.exception.AuthException;
import com.cherrydev.cherrymarketbe.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

import static com.cherrydev.cherrymarketbe.account.enums.RegisterType.KAKAO;
import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.*;
import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.BEARER_PREFIX;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressMapper addressMapper;
    private final AccountMapper accountMapper;
    private final RedisService redisService;
    private final RestTemplate restTemplate;

    private static final int MAX_ADDRESS_COUNT = 3;

    @Transactional
    public void addAddress(
            final AccountDetails accountDetails,
            final AddAddressRequestDto addAddressRequestDto
    ) {
        Account account = accountDetails.getAccount();
        CustomerAddress customerAddress = addAddressRequestDto.toEntity(account);

        checkAddressCount(account);
        checkDefaultAddressAlreadyExists(customerAddress);

        addressMapper.save(customerAddress);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<AddressInfoDto>> getAddress(
            final AccountDetails accountDetails
    ) {
        Account account = accountDetails.getAccount();
        return ResponseEntity.ok(addressMapper.findAllByAccountId(account));
    }

    public int addAddressFromKakao(final AccountDetails accountDetails) {
        checkAccountRegisterType(accountDetails);
        String oAuthAccessToken = getOAuthAccessToken(accountDetails);

        KakaoAddressInfoDto kakaoAddressInfoDto = getAddressesFromKakao(oAuthAccessToken);
        List<KakaoAddressInfoDto.KakaoAddress> kakaoAddress = kakaoAddressInfoDto.getKakaoAddress();

        for (KakaoAddressInfoDto.KakaoAddress address : kakaoAddress) {
            CustomerAddress customerAddress = kakaoAddressInfoDto.toEntity(accountDetails.getAccount(), address);
            addressMapper.save(customerAddress);
        }

        return kakaoAddress.size();
    }

    // ==================== PRIVATE METHODS ==================== //

    /**
     * 요청 계정이 소셜 계정인지 확인
     *
     * @param accountDetails 계정 정보
     */
    private void checkAccountRegisterType(
            final AccountDetails accountDetails
    ) {
        boolean isSocialAccount = accountMapper.existByEmailAndRegistType(accountDetails.getUsername(), KAKAO);
        if (!isSocialAccount) {
            throw new AuthException(NOT_SOCIAL_ACCOUNT);
        }
    }
    private void checkDefaultAddressAlreadyExists(final CustomerAddress customerAddress) {
        boolean isExist = addressMapper.existByAccountIdAndIsDefault(customerAddress);
        if (isExist) {
            throw new ServiceFailedException(DEFAULT_ADDRESS_ALREADY_EXISTS);
        }
    }
    private void checkAddressCount(final Account account) {
        int addressCount = addressMapper.countAllByAccountId(account);
        if (addressCount >= MAX_ADDRESS_COUNT) {
            throw new ServiceFailedException(ADDRESS_COUNT_EXCEEDED);
        }
    }

    private String getOAuthAccessToken(final AccountDetails accountDetails) {
        Account account = accountDetails.getAccount();
        String oAuthAccessToken = redisService.getData(OAUTH_KAKAO_PREFIX + account.getEmail());

        if (oAuthAccessToken == null) {
            throw new AuthException(NO_AUTHORIZATION);
        }

        return oAuthAccessToken;
    }

    /**
     * 카카오 API 를 통해 사용자의 주소 정보를 가져온다.
     * @param oAuthAccessToken 카카오 액세스 토큰
     * @return 사용자의 주소 정보
     */
    private KakaoAddressInfoDto getAddressesFromKakao(final String oAuthAccessToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_USER_ADDRESS_URL);

        HttpEntity<String> entity = createHttpEntity(oAuthAccessToken);
        ResponseEntity<KakaoAddressInfoDto> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, entity, KakaoAddressInfoDto.class
        );

        return KakaoAddressInfoDto.builder()
                .id(Objects.requireNonNull(response.getBody()).getId())
                .kakaoAddress(response.getBody().getKakaoAddress())
                .build();
    }

    private HttpEntity<String> createHttpEntity(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);
        return new HttpEntity<>("parameters", headers);
    }
}
