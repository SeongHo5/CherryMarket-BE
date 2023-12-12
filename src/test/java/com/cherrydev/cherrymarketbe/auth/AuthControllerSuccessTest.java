package com.cherrydev.cherrymarketbe.auth;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
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
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


import java.util.Objects;

import static com.cherrydev.cherrymarketbe.common.constant.EmailConstant.*;
import static com.cherrydev.cherrymarketbe.factory.AuthFactory.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@SpringBootTest
@Rollback
@Testcontainers
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class AuthControllerSuccessTest {

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

    @Container
    public static final GenericContainer smtpServer = new GenericContainer(DockerImageName.parse("mailhog/mailhog"))
            .withExposedPorts(1025, 8025);

    @DynamicPropertySource
    static void smtpProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", smtpServer::getHost);
        registry.add("spring.mail.port", () -> smtpServer.getMappedPort(1025));
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
                .andExpect(MockMvcResultMatchers.header().exists("Set-Cookie"))
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
    void 본인_인증_메일_발송_성공() throws Exception {
        // Given
        String email = "test@example.com";

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/send-email")
                        .secure(true)
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Send-Email-Success",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("본인 인증 이메일 발송 성공")));
    }

    @Test
    @Transactional
    void 비밀번호_재설정_메일_발송_성공() throws Exception {
        // Given
        String email = "test@example.com";

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/send-reset-email")
                        .secure(true)
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Send-Password-Reset-Email-Success",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("비밀번호 재설정 이메일 발송 성공")));
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
                        .secure(true)
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
    void 비밀번호_재설정_성공() throws Exception {
        // Given
        String email = "test@example.com";
        String code = "A1BK2S";

        // When & Then
        redisTemplate.opsForValue().set(PREFIX_PW_RESET + email, code);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/auth/verify-reset-email")
                        .secure(true)
                        .param("email", email)
                        .param("verificationCode", code))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Verify-Password-Reset-Success",
                        resourceDetails()
                                .tag("인증 관리")
                                .description("비밀번호 재설정 성공")));
    }

    @AfterEach
    void tearDown() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().serverCommands().flushDb();
    }

}
