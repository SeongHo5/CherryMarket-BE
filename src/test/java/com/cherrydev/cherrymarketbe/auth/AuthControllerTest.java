package com.cherrydev.cherrymarketbe.auth;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.cherrydev.cherrymarketbe.common.constant.EmailConstant.PREFIX_VERIFY;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.factory.AuthFactory.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
@Testcontainers
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    JwtProvider jwtProvider;

    private Account account;
    private AccountDetails accountDetails;
    private JwtResponseDto jwtResponseDto;
    private JwtRequestDto jwtRequestDto;

    @Container
    private static final RedisContainer container = new RedisContainer(
            RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", container::getHost);
        registry.add("spring.redis.port", container::getFirstMappedPort);
    }

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AccountDetails) {
            accountDetails = (AccountDetails) authentication.getPrincipal();
            jwtResponseDto = jwtProvider.createJwtToken(accountDetails.getUsername());
            jwtRequestDto = new JwtRequestDto(jwtResponseDto.getAccessToken(), jwtResponseDto.getRefreshToken());
            account = accountDetails.getAccount();
        }
    }

    @Test
    @Transactional
    void 로그인_성공() throws Exception {
        // Given
        SignInRequestDto signInRequestDto = createSignInRequestDtoA();
        String requestBody = Jackson.toJsonString(signInRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userRole").value("ROLE_CUSTOMER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grantType").value("Bearer "))
                .andDo(document("Sign-In-Success",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("로그인 성공"),
                        responseFields(
                                fieldWithPath("userName").description("사용자 이름"),
                                fieldWithPath("userRole").description("사용자 권한"),
                                fieldWithPath("grantType").description("토큰 타입"),
                                fieldWithPath("accessToken").description("액세스 토큰"),
                                fieldWithPath("refreshToken").description("리프레시 토큰"),
                                fieldWithPath("expiresIn").description("토큰 만료 시간")
                        )));
    }

    @Test
    @Transactional
    void 로그인_실패_비밀번호_불일치() throws Exception {
        // Given
        SignInRequestDto signInRequestDto = createSignInRequestDtoB();
        String requestBody = Jackson.toJsonString(signInRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(document("Sign-In-Fail-Invalid-ID-or-PW",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("로그인 실패 - ID 혹은 PW 불일치"),
                        responseFields(
                                fieldWithPath("statusCode").description(INVALID_ID_OR_PW.getStatusCode()),
                                fieldWithPath("message").description(INVALID_ID_OR_PW.getMessage())
                        )));
    }

    @Test
    @Transactional
    void 로그인_실패_존재하지_않는_이메일() throws Exception {
        // Given
        SignInRequestDto signInRequestDto = createSignInRequestDtoC();
        String requestBody = Jackson.toJsonString(signInRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(document("Sign-In-Fail-Not-Registered",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("로그인 실패 - 존재하지 않는 이메일"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_FOUND_ACCOUNT.getStatusCode()),
                                fieldWithPath("message").description(NOT_FOUND_ACCOUNT.getMessage())
                        )));
    }

    @Test
    @Transactional
    void 로그인_실패_제한된_사용자() throws Exception {
        // Given
        SignInRequestDto signInRequestDto = createSignInRequestDtoD();
        String requestBody = Jackson.toJsonString(signInRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(document("Sign-In-Fail-Restricted",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("로그인 실패 - 제한된 사용자"),
                        responseFields(
                                fieldWithPath("statusCode").description(RESTRICTED_ACCOUNT.getStatusCode()),
                                fieldWithPath("message").description(RESTRICTED_ACCOUNT.getMessage())
                        )));
    }

    @Test
    @Transactional
    void 로그인_실패_탈퇴한_사용자() throws Exception {
        // Given
        SignInRequestDto signInRequestDto = createSignInRequestDtoE();
        String requestBody = Jackson.toJsonString(signInRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/sign-in")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andDo(document("Sign-In-Fail-Deleted",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("로그인 실패 - 탈퇴한 사용자"),
                        responseFields(
                                fieldWithPath("statusCode").description(DELETED_ACCOUNT.getStatusCode()),
                                fieldWithPath("message").description(DELETED_ACCOUNT.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "yeongsun80@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 로그아웃_성공() throws Exception {
        // Given
        String requestBody = Jackson.toJsonString(jwtRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/auth/sign-out")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Sign-Out-Success",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("로그아웃 성공")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ksong@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 토큰_재발급_성공() throws Exception {
        // Given
        String email = accountDetails.getUsername();
        String requestBody = Jackson.toJsonString(jwtRequestDto);

        // When & Then
        redisTemplate.opsForValue().set(email, jwtResponseDto.getRefreshToken());
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/re-issue")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessTokenExpiresIn").exists())
                .andDo(document("Reissue-Token-Success",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("토큰 재발급 성공"),
                        responseFields(
                                fieldWithPath("accessToken").description("새로 발급된 액세스 토큰"),
                                fieldWithPath("refreshToken").description("새로 발급된 리프레시 토큰"),
                                fieldWithPath("accessTokenExpiresIn").description("토큰 만료 시간")
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "ksong@example.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 토큰_재발급_실패_잘못된_토큰() throws Exception {
        // Given
        String email = accountDetails.getUsername();
        String requestBody = Jackson.toJsonString(jwtRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/re-issue")
                        .secure(true)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Reissue-Token-Fail-Invalid-Token",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("토큰 재발급 실패 - 잘못된 토큰"),
                        responseFields(
                                fieldWithPath("statusCode").description(INVALID_AUTH_ERROR.getStatusCode()),
                                fieldWithPath("message").description(INVALID_AUTH_ERROR.getMessage())
                        )));
    }

    @Test
    @Transactional
    void 본인_인증_성공() throws Exception {
        // Given
        String email = "test@example.com";
        String verificationCode = "A1BK2S";

        // When & Then
        redisTemplate.opsForValue().set(PREFIX_VERIFY + email, verificationCode);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/auth/verify-email")
                        .param("email", email)
                        .param("verificationCode", verificationCode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Verify-Email-Success",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("본인 인증 성공")));
    }

    @Test
    @Transactional
    void 본인_인증_실패_잘못된_이메일() throws Exception {
        // Given
        String email = "wrong@example.com";
        String wrongEmail = "badmail@example.com";
        String verificationCode = "123456";

        // When & Then
        redisTemplate.opsForValue().set(PREFIX_VERIFY + email, verificationCode);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/auth/verify-email")
                        .param("email", wrongEmail)
                        .param("verificationCode", verificationCode))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(document("Verify-Email-Fail-Wrong-Email",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("본인 인증 실패 - 잘못된 이메일"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_FOUND_REDIS_KEY.getStatusCode()),
                                fieldWithPath("message").description(NOT_FOUND_REDIS_KEY.getMessage())
                        )));
    }

    @Test
    @Transactional
    void 본인_인증_실패_잘못된_인증코드() throws Exception {
        // Given
        String email = "test@example.com";
        String verificationCode = "654321";

        // When & Then
        redisTemplate.opsForValue().set(PREFIX_VERIFY + email, "123456");
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/auth/verify-email")
                        .param("email", email)
                        .param("verificationCode", verificationCode))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Verify-Email-Fail-Wrong-Code",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("본인 인증 실패 - 잘못된 인증코드"),
                        responseFields(
                                fieldWithPath("statusCode").description(INVALID_EMAIL_VERIFICATION_CODE.getStatusCode()),
                                fieldWithPath("message").description(INVALID_EMAIL_VERIFICATION_CODE.getMessage())
                        )));
    }
}
