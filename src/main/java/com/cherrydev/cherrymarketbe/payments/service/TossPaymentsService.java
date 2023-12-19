package com.cherrydev.cherrymarketbe.payments.service;

import com.cherrydev.cherrymarketbe.payments.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentConfirmForm;

import com.cherrydev.cherrymarketbe.payments.model.cardpromotion.CardPromotion;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

@Service
@Transactional(readOnly = true)
@Slf4j
public class TossPaymentsService {

    private static final String BASE_URL = "https://api.tosspayments.com";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String clientSecret;

    public TossPaymentsService(@Value("${toss.payment.clientkey}") String clientSecret) {
        this.clientSecret = clientSecret;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        this.restTemplate = this.buildRestTemplate(objectMapper);
    }

    private RestTemplate buildRestTemplate(ObjectMapper objectMapper) {

        return new RestTemplateBuilder()
                .rootUri(BASE_URL)
                .setReadTimeout(DEFAULT_TIMEOUT)
                .additionalInterceptors((request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    headers.set("Authorization", authorization());
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    return execution.execute(request, body);
                })
                .messageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8),
                        new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    /**
     * 결제 조회
     */
    @Transactional
    public Payment findPaymentByOrderId(String orderCode) {
        String uri = "/v1/payments/orders/" + orderCode;
        return getForObject(uri, Payment.class);
    }

    /**
     * 결제 승인
     */
    @Transactional
    public Payment paymentConfirm(PaymentConfirmForm form) {
        String uri = "/v1/payments/confirm";
        return postForObject(uri, form.toRequestBody(), Payment.class);
    }

    /**
     * 결제 취소
     * @param paymentKey
     * @param form
     * @return Payment
     */
    @Transactional
    public Payment paymentCancel(String paymentKey, PaymentCancelForm form) {
        String uri = "/v1/payments/" + paymentKey + "/cancel";
        return postForObject(uri, form.toRequestBody(), Payment.class);
    }

    /**
     * 카드 혜택 조회
     */
    public CardPromotion cardPromotion() {
        String uri = "/v1/promotions/card";
        return getForObject(uri, CardPromotion.class);
    }

    private String authorization() {
        return "Basic " + Base64.getEncoder().encodeToString((clientSecret + ":").getBytes());
    }

    private <T> T getForObject(String uri, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, clazz);
        return responseEntity.getBody();
    }

    private <R, T> T postForObject(String uri, R form, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization());
        HttpEntity<R> httpEntity = new HttpEntity<>(form, headers);
        ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, clazz);
        return responseEntity.getBody();
    }

}