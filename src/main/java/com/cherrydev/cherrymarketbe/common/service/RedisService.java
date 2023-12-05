package com.cherrydev.cherrymarketbe.common.service;

import com.cherrydev.cherrymarketbe.auth.dto.oauth.OAuthTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.OAUTH_KAKAO_PREFIX;
import static com.cherrydev.cherrymarketbe.common.constant.AuthConstant.OAUTH_KAKAO_REFRESH_PREFIX;


@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * Redis에 저장된 데이터(Key:Value) 가져오기
     * @param key 가져올 데이터의 Key
     * @return 데이터
     */
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * Redis에 데이터 저장
     * <p>
     * <em>만료 시간을 설정하지 않으면 영구적으로 저장되니 신중하게 사용해야 한다.</em>
     * @param key 저장할 데이터의 Key
     * @param value 저장할 데이터
     */
    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    /**
     * Redis에 데이터 저장 및 만료 시간 설정
     * @param key 저장할 데이터의 Key
     * @param value 저장할 데이터
     * @param duration 만료 시간
     */
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    /**
     * Redis에 해당 Key를 가진 데이터가 존재하는지 확인
     * @param key 확인할 데이터의 Key
     * @return 데이터 존재 여부
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    /**
     * Redis에 저장된 데이터 삭제
     * @param key 삭제할 데이터의 Key
     */
    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

    // ==================== REDIS - OAUTH ==================== //
    /**
     * OAuth 인증 토큰을 Redis에 저장한다.
     *
     * @param oAuthTokenResponseDto OAuth 인증 토큰
     * @param email                 사용자 이메일
     */
    public void saveTokenToRedis(
            final OAuthTokenResponseDto oAuthTokenResponseDto,
            final String email
    ) {
        String accessToken = oAuthTokenResponseDto.getAccessToken();
        Long expiresIn = oAuthTokenResponseDto.getExpiresIn();

        String refreshToken = oAuthTokenResponseDto.getRefreshToken();
        Long refreshTokenExpiresIn = oAuthTokenResponseDto.getRefreshTokenExpiresIn();

        setDataExpire(OAUTH_KAKAO_PREFIX + email, accessToken, expiresIn);
        setDataExpire(OAUTH_KAKAO_REFRESH_PREFIX + email, refreshToken, refreshTokenExpiresIn);
    }
    /**
     * Redis에 저장된 OAuth 인증 토큰을 삭제한다.
     *
     * @param email 사용자 이메일
     */
    public void deleteKakaoTokenFromRedis(final String email) {
        deleteData(OAUTH_KAKAO_PREFIX + email);
        deleteData(OAUTH_KAKAO_REFRESH_PREFIX + email);
    }
}
