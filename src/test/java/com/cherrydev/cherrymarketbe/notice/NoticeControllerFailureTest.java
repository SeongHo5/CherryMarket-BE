package com.cherrydev.cherrymarketbe.notice;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.RequestSignIn;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.factory.NoticeFactory;
import com.cherrydev.cherrymarketbe.notice.dto.NoticeRequestDto;
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

import static com.cherrydev.cherrymarketbe.factory.AuthFactory.createSignInRequestDtoF;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;


@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class NoticeControllerFailureTest {

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
    void 공지사항_등록_실패_카테고리_누락() throws Exception {
        // Given
        NoticeRequestDto noticeRequestDto = NoticeFactory.createNoticeB();
        String requestBody = Jackson.toJsonString(noticeRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/notice/add-notice")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Failed-Add-Notice-Empty-Category",
                        resourceDetails()
                                .tag("공지사항")
                                .description("공지사항 등록 실패 - 카테고리"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CATEGORY.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CATEGORY.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 공지사항_등록_실패_제목_누락() throws Exception {
        // Given
        NoticeRequestDto noticeRequestDto = NoticeFactory.createNoticeC();
        String requestBody = Jackson.toJsonString(noticeRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/notice/add-notice")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Failed-Add-Notice-Empty-Subject",
                        resourceDetails()
                                .tag("공지사항")
                                .description("공지사항 등록 실패 - 제목"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_SUBJECT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_SUBJECT.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 공지사항_등록_실패_내용_누락() throws Exception {
        // Given
        NoticeRequestDto noticeRequestDto = NoticeFactory.createNoticeD();
        String requestBody = Jackson.toJsonString(noticeRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/notice/add-notice")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andDo(document("Failed-Add-Notice-Empty-Content",
                        resourceDetails()
                                .tag("공지사항")
                                .description("공지사항 등록 실패 - 내용"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CONTENT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CONTENT.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "admin@devcherry.com", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 공지사항_등록_실패_게시날짜_누락() throws Exception {
        // Given
        NoticeRequestDto noticeRequestDto = NoticeFactory.createNoticeE();
        String requestBody = Jackson.toJsonString(noticeRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/notice/add-notice")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andDo(document("Failed-Add-Notice-Empty-Date",
                        resourceDetails()
                                .tag("공지사항")
                                .description("공지사항 등록 실패 - 날짜"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CATEGORY.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CATEGORY.getMessage())
                        )));
    }

}

