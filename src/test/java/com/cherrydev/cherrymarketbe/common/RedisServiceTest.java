package com.cherrydev.cherrymarketbe.common;

import com.cherrydev.cherrymarketbe.common.service.RedisService;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
public class RedisServiceTest {

    @Container
    private static final RedisContainer container = new RedisContainer(
            RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", container::getHost);
        registry.add("spring.redis.port", container::getFirstMappedPort);
    }

    @Autowired
    private RedisService redisService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void 데이터_가져오기_성공() {
        // Given
        String key = "testKey";
        String value = "testValue";
        redisService.setData(key, value);

        // When & Then
        assertThat(redisService.getData(key)).isEqualTo(value);
    }

    @Test
    void 데이터_저장_성공() {
        // Given
        String key = "testKey";
        String value = "testValue";

        // When
        redisService.setData(key, value);

        // Then
        assertThat(redisService.getData(key)).isEqualTo(value);
    }

    @Test
    void 데이터_저장_만료_시간_설정_성공() throws Exception {
        // Given
        String key = "testKey";
        String value = "testValue";
        long duration = 1L;

        // When
        redisService.setDataExpire(key, value, duration);

        // Then
        assertThat(redisService.getData(key)).isEqualTo(value);
        Thread.sleep(duration * 1000 + 1);
        assertThat(redisService.getData(key)).isNull();
    }

    @Test
    void 키_존재_확인_성공() {
        // Given
        String key = "testKey";
        String value = "testValue";
        redisService.setData(key, value);

        // When & Then
        assertThat(redisService.hasKey(key)).isTrue();
    }

    @Test
    void 데이터_삭제_성공() {
        // Given
        String key = "testKey";
        String value = "testValue";
        redisService.setData(key, value);

        // When
        redisService.deleteData(key);

        // Then
        assertThat(redisService.getData(key)).isNull();
    }

    @AfterEach
    void tearDown() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().serverCommands().flushDb();
    }

}
