package com.cherrydev.cherrymarketbe.inquiry;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.inquiry.dto.InquiryRequestDto;
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

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
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
class InquiryControllerFailureTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    JwtProvider jwtProvider;


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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
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
                .andDo(document("Failed-Add-Inquiry-Empty-Category",
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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
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
                .andDo(document("Failed-Add-Inquiry-Empty-Detail-Category",
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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
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
                .andDo(document("Failed-Add-Inquiry-Empty-Subject",
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
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
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
                .andDo(document("Failed-Add-Inquir-Empty-Content",
                        resourceDetails()
                                .tag("1:1문의")
                                .description("1:1문의 등록 실패 - 내용"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CONTENT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CONTENT.getMessage())
                        )));
    }


}

