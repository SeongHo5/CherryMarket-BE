package com.cherrydev.cherrymarketbe.InquiryAnswer;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.RequestSignIn;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.factory.InquiryAnswerFactory;
import com.cherrydev.cherrymarketbe.inquiryanswer.dto.InquiryAnwerRequestDto;
import com.cherrydev.cherrymarketbe.notice.entity.Notice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.ALREADY_EXIST_ANSWER;
import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_ALLOWED_EMPTY_CONTENT;
import static com.cherrydev.cherrymarketbe.factory.AuthFactory.createSignInRequestDtoF;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;


@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class InquiryAnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    JwtProvider jwtProvider;

    private Notice notice;

    private Account account;
    private AccountDetails accountDetails;
    private JwtResponseDto jwtResponseDto;
    private JwtRequestDto jwtRequestDto;


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
        RequestSignIn requestSignIn = createSignInRequestDtoF();
        String requestBody = Jackson.toJsonString(requestSignIn);

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
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의답변_등록_성공() throws Exception {
        // Given
        InquiryAnwerRequestDto inquiryAnwerRequestDto = InquiryAnswerFactory.createInquiryAnswerA();
        String requestBody = Jackson.toJsonString(inquiryAnwerRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/inquiryAnswer/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(document("Add-Inquiry-Answer-Success",
                        resourceDetails()
                                .tag("문의 답변")
                                .description("문의 답변 등록 성공")
                        ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의답변_등록_실패_내용누락() throws Exception {
        // Given
        InquiryAnwerRequestDto inquiryAnwerRequestDto = InquiryAnswerFactory.createInquiryAnswerB();
        String requestBody = Jackson.toJsonString(inquiryAnwerRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/inquiryAnswer/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andDo(document("Failed-Add-Inquiry-Answer-Empty-Contetnt",
                        resourceDetails()
                                .tag("문의 답변")
                                .description("문의 답변 등록 실패 - 내용누락"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CONTENT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CONTENT.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의답변_등록_실패_중복등록() throws Exception {
        // Given
        InquiryAnwerRequestDto inquiryAnwerRequestDto = InquiryAnswerFactory.createInquiryAnswerC();
        String requestBody = Jackson.toJsonString(inquiryAnwerRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/inquiryAnswer/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Failed-Add-Inquiry-Answer-Duplicate",
                        resourceDetails()
                                .tag("문의 답변")
                                .description("문의 답변 등록 실패 - 중복등록"),
                        responseFields(
                                fieldWithPath("statusCode").description(ALREADY_EXIST_ANSWER.getStatusCode()),
                                fieldWithPath("message").description(ALREADY_EXIST_ANSWER.getMessage())
                        )));
    }
}

