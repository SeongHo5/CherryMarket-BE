package com.cherrydev.cherrymarketbe.inquiry;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.ModifyInquiryRequestDto;
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
import org.springframework.restdocs.payload.JsonFieldType;
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

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.factory.AuthFactory.createSignInRequestDtoA;
import static com.cherrydev.cherrymarketbe.factory.InquiryFactory.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;


@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class InquiryControllerTest {

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
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_등록_성공() throws Exception {
        // Given
        InquiryRequestDto inquiryRequestDto = createInquiryA();
        String requestBody = Jackson.toJsonString(inquiryRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/inquiry/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(document("Add-Inquiry-Success",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("1:1문의 등록 성공")
                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_등록_실패_카테고리_누락() throws Exception {
        // Given
        InquiryRequestDto inquiryRequestDto = createInquiryB();
        String requestBody = Jackson.toJsonString(inquiryRequestDto);
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/notice/add-notice")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andDo(document("Add-Inquiry-Failed-Null-Category",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("1:1문의 등록 실패 - 카테고리"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CATEGORY.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CATEGORY.getMessage())
                        )));

    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_등록_실패_세부카테고리_누락() throws Exception {
        // Given
        InquiryRequestDto inquiryRequestDto = createInquiryC();
        String requestBody = Jackson.toJsonString(inquiryRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/notice/add-notice")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andDo(document("Add-Inquiry-Failed-Null-Detail-Category",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("1:1문의 등록 실패 - 세부카테고리"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_DETAIL_CATEGORY.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_DETAIL_CATEGORY.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_등록_실패_제목_누락() throws Exception {
        // Given
        InquiryRequestDto inquiryRequestDto = createInquiryD();
        String requestBody = Jackson.toJsonString(inquiryRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/notice/add-notice")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andDo(document("Add-Inquiry-Failed-Null-Subject",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("1:1문의 등록 실패 - 제목"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_SUBJECT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_SUBJECT.getMessage())
                        )));

    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 공지사항_등록_실패_내용_누락() throws Exception {
        // Given
        InquiryRequestDto inquiryRequestDto = createInquiryE();
        String requestBody = Jackson.toJsonString(inquiryRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/notice/add-notice")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andDo(document("Add-Inquiry-Failed-Null-Content",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("1:1문의 등록 실패 - 내용"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CONTENT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CONTENT.getMessage())
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_조회_아이디_성공() throws Exception {

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/inquiry/search-id?inquiryId=39")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiryId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").exists())
                .andDo(document("Get-Inquiry-Info-Id",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("조회 - 아이디")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_조회_코드_성공() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/inquiry/search-code?inquiryCode=INQ8")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiryId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").exists())
                .andDo(document("Get-Inquiry-Info-Code",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("조회 - 코드")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_전체조회_성공() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/inquiry/list")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andDo(document("Get-List-Inquiry-Info",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("전체 조회")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_전체조회_코드_성공() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/inquiry/list/user?userId=139")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andDo(document("Get-List-Inquiry-Info-Code",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("전체 조회 - 코드")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_전체조회_회원ID_성공() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/inquiry/list/user?userId=13")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andDo(document("Get-List-Inquiry-Info-UserId",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("전체 조회 - 회원 아이디")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_전체조회_회원Phone_성공() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/inquiry/list/phone?phone=010-2409-1324")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andDo(document("Get-List-Inquiry-Info-UserPhone",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("전체 조회 - 회원 핸드폰")

                ));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_삭제_아이디_성공() throws Exception {
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/inquiry/delete/id?inquiryId=39")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Delete-Inquiry-By-ID",
                        resourceDetails()
                                .tag("1:1 문의")
                                .description("삭제 - 아이디")));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_삭제_코드_성공() throws Exception {
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/inquiry/delete/code?inquiryCode=INQ10")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("Delete-Inquiry-By-Code",
                        resourceDetails()
                                .tag("1:1 문의")
                                .description("삭제 - 코드")));
    }


    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_수정_아이디_성공() throws Exception {
        // Given
        ModifyInquiryRequestDto modifyInquiryRequestDto = createModifyInquiryA();
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/inquiry/modify-id")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jackson.toJsonString(modifyInquiryRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiryId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").exists())
                .andDo(document("Modify-Inquiry-Info-By-ID",
                        resourceDetails()
                                .tag("1:1 문의")
                                .description("1:1문의 수정 - 아이디"),
                        responseFields(
                                fieldWithPath("inquiryId").optional().description("문의 ID"),
                                fieldWithPath("userId").optional().description("사용자 ID"),
                                fieldWithPath("code").optional().description("문의 코드"),
                                fieldWithPath("type").optional().description("문의 유형"),
                                fieldWithPath("detailType").optional().description("문의상세유형"),
                                fieldWithPath("subject").optional().description("문의 제목"),
                                fieldWithPath("content").optional().description("문의 내용"),
                                fieldWithPath("status").optional().type(JsonFieldType.STRING).description("문의답변 상태"),
                                fieldWithPath("answerStatus").optional().type(JsonFieldType.STRING).description("문의 상태"),
                                fieldWithPath("phone").optional().description("문의 수신 휴대폰 번호"),
                                fieldWithPath("createDate").optional().type(JsonFieldType.STRING).description("문의 생성일"),
                                fieldWithPath("deleteDate").optional().type(JsonFieldType.STRING).description("문의삭제일")
                        )));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "boram17@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 문의_수정_코드_성공() throws Exception {
        // Given
        ModifyInquiryRequestDto modifyInquiryRequestDto = createModifyInquiryB();
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/inquiry/modify-code")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Jackson.toJsonString(modifyInquiryRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiryId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").exists())
                .andDo(document("Modify-Inquiry-Info-By-Code",
                        resourceDetails()
                                .tag("1:1 문의")
                                .description("1:1문의 수정 - 코드"),
                        responseFields(
                                fieldWithPath("inquiryId").optional().description("문의 ID"),
                                fieldWithPath("userId").optional().description("사용자 ID"),
                                fieldWithPath("code").optional().description("문의 코드"),
                                fieldWithPath("type").optional().description("문의 유형"),
                                fieldWithPath("detailType").optional().description("문의상세유형"),
                                fieldWithPath("subject").optional().description("문의 제목"),
                                fieldWithPath("content").optional().description("문의 내용"),
                                fieldWithPath("status").optional().type(JsonFieldType.STRING).description("문의답변 상태"),
                                fieldWithPath("answerStatus").optional().type(JsonFieldType.STRING).description("문의 상태"),
                                fieldWithPath("phone").optional().description("문의 수신 휴대폰 번호"),
                                fieldWithPath("createDate").optional().type(JsonFieldType.STRING).description("문의 생성일"),
                                fieldWithPath("deleteDate").optional().type(JsonFieldType.STRING).description("문의삭제일")
                        )));
    }

}

