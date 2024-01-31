package com.cherrydev.cherrymarketbe.GoodsReviewReport;

import com.amazonaws.util.json.Jackson;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.dto.JwtRequestDto;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.dto.JwtResponseDto;
import com.cherrydev.cherrymarketbe.goodsreview.entity.GoodsReview;
import com.cherrydev.cherrymarketbe.goodsreviewreport.dto.ReviewReportModifyDto;
import com.cherrydev.cherrymarketbe.goodsreviewreport.dto.ReviewReportRequestDto;
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

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.ALREADY_EXIST_REPORT;
import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_ALLOWED_EMPTY_CONTENT;
import static com.cherrydev.cherrymarketbe.factory.GoodsReviewReportFactory.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;


@Rollback
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@SpringBootTest
class GoodsReviewReportControllerFailureTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    JwtProvider jwtProvider;

    private GoodsReview goodsReview;

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
    void 상품후기_신고_중복_실패() throws Exception {
        // Given
        ReviewReportRequestDto reviewReportRequestDto = createReviewReportRequestDtoF();
        String requestBody = Jackson.toJsonString(reviewReportRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/review-report/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Failed-Add-GoodsReviewReport-Duplicated-Exception",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품후기 신고 등록 실패 - 중복 신고"),
                        responseFields(
                                fieldWithPath("statusCode").description(ALREADY_EXIST_REPORT.getStatusCode()),
                                fieldWithPath("message").description(ALREADY_EXIST_REPORT.getMessage())
                        )));

    }


    @Test
    @Transactional
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_등록_실패_내용누락() throws Exception {
        // Given
        ReviewReportRequestDto reviewReportRequestDto = createReviewReportRequestDtoB();
        String requestBody = Jackson.toJsonString(reviewReportRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/goods-review/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Failed-Add-GoodsReview-Duplicated-Exception",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품 후기 등록 실패 - 내용 누락"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CONTENT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CONTENT.getMessage())
                        )));

    }

    @Test
    @Transactional
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_등록_실패_카테고리_누락() throws Exception {
        // Given
        ReviewReportRequestDto reviewReportRequestDto = createReviewReportRequestDtoC();
        String requestBody = Jackson.toJsonString(reviewReportRequestDto);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/goods-review/add")
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .secure(true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Failed-Add-GoodsReview-Duplicated-Exception",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품 후기 등록 실패 - 카테고리 누락"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CONTENT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CONTENT.getMessage())
                        )));

    }


    @Test
    @Transactional
    @WithUserDetails(value = "noyeongjin@example.org", userDetailsServiceBeanName = "accountDetailsServiceImpl")
    void 상품후기_신고_답변_등록_실패() throws Exception {
        // Given
        ReviewReportModifyDto reviewReportModifyDto = createReviewReportRequestDtoE();
        String requestBody = Jackson.toJsonString(reviewReportModifyDto);
        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/review-report/add-answer")
                        .secure(true)
                        .header("Authorization", "Bearer " + jwtResponseDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(document("Failed-Modify-GoodsReviewReport-Answer",
                        resourceDetails()
                                .tag("상품후기 신고")
                                .description("상품후기 신고 답변 - 등록 실패 - 답변 누락"),
                        responseFields(
                                fieldWithPath("statusCode").description(NOT_ALLOWED_EMPTY_CONTENT.getStatusCode()),
                                fieldWithPath("message").description(NOT_ALLOWED_EMPTY_CONTENT.getMessage())
                        )));
    }
}

