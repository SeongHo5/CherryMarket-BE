package com.cherrydev.cherrymarketbe.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.sql.Timestamp;
import java.time.LocalDate;

import static com.cherrydev.cherrymarketbe.server.application.common.constant.AuthConstant.AUTHORIZATION_HEADER;
import static com.cherrydev.cherrymarketbe.server.application.common.constant.EmailConstant.VERIFICATION_CODE_LENGTH;
import static com.cherrydev.cherrymarketbe.server.application.common.utils.HttpEntityUtils.createHttpEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class UtilTest {

    @Test
    void 코드_생성_테스트() {
        // Given
        String case1 = generateRandomCode(VERIFICATION_CODE_LENGTH);
        String case2 = generateRandomPassword(VERIFICATION_CODE_LENGTH);

        // When & Then
        assertThat(case1.length()).isEqualTo(VERIFICATION_CODE_LENGTH);
        assertThat(case2.length()).isEqualTo(VERIFICATION_CODE_LENGTH);
    }

    @Test
    void 형변환_테스트() {
        // Given
        LocalDate case1 = LocalDate.of(2021, 10, 10);
        Timestamp case2 = Timestamp.valueOf("2021-10-10 10:10:10");

        // When
        String result1 = localDateToString(case1);
        String result2 = timeStampToString(case2);

        // Then
        assertThat(result1).isEqualTo(case1.toString());
        assertThat(result2).isEqualTo("2021-10-10 10:10:10");
    }

    @Test
    void HTTP_엔티티_생성_성공() {
        // When
        HttpEntity<String> httpEntity = createHttpEntity();

        // Then
        HttpHeaders headers = httpEntity.getHeaders();
        assertThat(headers.getContentType()).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED);
        assertThat(headers.get(AUTHORIZATION_HEADER)).isNull();
    }

    @Test
    void HTTP_엔티티_생성_성공_토큰_포함() {
        // Given
        String accessToken = "testToken";

        // When
        HttpEntity<String> httpEntity = createHttpEntity(accessToken);

        // Then
        HttpHeaders headers = httpEntity.getHeaders();
        assertThat(headers.getContentType()).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED);
        assertThat(headers.get(AUTHORIZATION_HEADER)).isNotNull();
    }


}
